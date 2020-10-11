package four.classd.cd.controller;

import four.classd.cd.annotation.Authorize;
import four.classd.cd.config.SwaggerConfig;
import four.classd.cd.dao.*;
import four.classd.cd.model.entity.*;
import four.classd.cd.model.enums.ExceptionType;
import four.classd.cd.model.enums.ResourceType;
import four.classd.cd.model.enums.UserOrderStatus;
import four.classd.cd.model.vo.ResultVO;
import four.classd.cd.service.StationService;
import four.classd.cd.util.KeyUtil;
import four.classd.cd.util.ResultVOUtil;
import four.classd.cd.util.TypeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.events.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author PusenYang
 * @version 1.0
 * @date 2020/10/3 16:54
 */
@RestController
@RequestMapping("/d_order")
@Slf4j
@Api(tags = {SwaggerConfig.ORDER_TAG})
@CrossOrigin(origins = "*")
public class DesignOrderController {

    @Autowired
    private DesignOrderDao designOrderDao;

    @Autowired
    private ReceiveStationDao receiveStationDao;

    @Autowired
    private DesignStationDao designStationDao;

    @Autowired
    private DesignStationManagerDao designStationManagerDao;

    @Autowired
    private ReceiveStationManagerDao receiveStationManagerDao;

    @Autowired
    private ResourceDao resourceDao;

    @Autowired
    private StationService stationService;

    @Autowired
    private GoManagerDao goManagerDao;

    @Autowired
    private UserOrderDao userOrderDao;

    @ResponseBody
    @GetMapping("/advice")
    @ApiOperation(value = "获取调配建议")
    public ResultVO getAdvice(@RequestParam(value = "token",required = false)String token) {
        DesignStationManager dsm = designStationManagerDao.findByToken(token);
        if (dsm == null) {
            log.info(">>>获取调配建议 不是负责人");
            return ResultVOUtil.error(ExceptionType.AUTHORITY_ERROR.getCode(),"您还不是调配站负责人, 无法进行此操作");
        }
        int did = dsm.getStationId();
        String advice = stationService.genAdvice(did);
        log.info(">>>创建调配单 成功获取建议");
        return ResultVOUtil.success(advice);
    }

    /**
     * 调配站创建订单
     * @param map
     * @return
     */
    @ResponseBody
    @PostMapping("/create")
    @ApiOperation(value = "创建调配单")
    public ResultVO createDesignOrder(@RequestBody(required = true)Map<String,Object> map) {
        /* 订单参数 */
        String token = map.get("token").toString();
        DesignStationManager dsm = designStationManagerDao.findByToken(token);
        if (dsm == null) {
            log.info(">>>d创建调配单 没有此负责人");
            return ResultVOUtil.error(ExceptionType.PARAM_ERROR.getCode(), "您还不是负责人, 无法创建调配单");
        }
        Integer goId = Integer.parseInt(map.get("go_manager_id").toString());
        GoManager goManager = goManagerDao.findById(goId);
        String goManagerName = goManager.getName();
        String goManagerPhone = goManager.getPhone();
        int designStationId = dsm.getStationId();
        String receiveName = map.get("receive_name").toString();
        ReceiveStation receiveStation = receiveStationDao.findByName(receiveName);
        String startDate = map.get("start_date").toString();

        String number = KeyUtil.generateNumber();
        int id = KeyUtil.generateOrdID();
        // 资源
        int totalAmount = 0;
        int totalWeight = 0;
        // n95
        Integer n95 = TypeUtil.getNumber(Integer.parseInt(map.get("amount_95").toString()));
        Integer pm25 = TypeUtil.getNumber(Integer.parseInt(map.get("amount_25").toString()));
        Integer ori = TypeUtil.getNumber(Integer.parseInt(map.get("amount_ori").toString()));

        totalAmount = n95 + pm25 + ori;
        totalWeight = n95 * resourceDao.getWeight(ResourceType.N95.getCode())
                + pm25 * resourceDao.getWeight(ResourceType.PM25.getCode())
                + ori * resourceDao.getWeight(ResourceType.Ori.getCode());
        designOrderDao.addResource(new DesignOrderResource(number,ResourceType.N95.getCode(),n95));
        designOrderDao.addResource(new DesignOrderResource(number,ResourceType.PM25.getCode(),pm25));
        designOrderDao.addResource(new DesignOrderResource(number,ResourceType.Ori.getCode(),ori));

        DesignStation designStation = designStationDao.findById(designStationId);
        System.out.println(receiveStation.getId());
        ReceiveStationManager receiveStationManager = receiveStationManagerDao.findByStation(receiveStation.getId());

        DesignOrder designOrder = new DesignOrder();
        designOrder.setId(id);
        designOrder.setNumber(number);
        designOrder.setStatus(UserOrderStatus.GOING.getCode());
        designOrder.setGoManagerName(goManagerName); // 运输人的名字
        designOrder.setGoManagerPhone(goManagerPhone); // 运输人的电话
        designOrder.setDesignManagerId(dsm.getId());
        designOrder.setDesignManagerName(dsm.getUsername());
        designOrder.setDesignManagerPhone(dsm.getPhone());
        designOrder.setReceiveManagerId(receiveStationManager.getId());
        designOrder.setReceiveManagerName(receiveStationManager.getUsername());
        designOrder.setReceiveManagerPhone(receiveStationManager.getPhone());
        designOrder.setTotalWeight(totalWeight);
        designOrder.setTotalAmount(totalAmount);
        designOrder.setStartId(designStationId);
        designOrder.setStartName(designStation.getName());
        designOrder.setStartAddress(designStation.getAddress());
        designOrder.setStartDate(startDate);
        designOrder.setEndId(receiveStation.getId());
        designOrder.setEndName(receiveName);
        designOrder.setEndAddress(receiveStation.getAddress());
        designOrderDao.addOrder(designOrder);

        /* 从当前调配站资源扣除运送的物资 */
        int re1 = designStationDao.getAmount(designStationId,ResourceType.N95.getCode());
        int re2 = designStationDao.getAmount(designStationId,ResourceType.PM25.getCode());
        int re3 = designStationDao.getAmount(designStationId,ResourceType.Ori.getCode());
        designStationDao.updateResource(designStationId,ResourceType.N95.getCode(),re1-n95);
        designStationDao.updateResource(designStationId,ResourceType.PM25.getCode(),re2-pm25);
        designStationDao.updateResource(designStationId,ResourceType.Ori.getCode(),re3-ori);

        log.info(">>>物资单 创建成功");
        return ResultVOUtil.success();
    }

    /**
     * 接收站确认收到物资
     * 同时需要更新物资单的状态
     * @param map
     * @return
     */
    @ResponseBody
    @PostMapping("/receive_verify")
    @ApiOperation(value = "接收站确认收到物资")
    public ResultVO userVerify(@RequestBody(required = true)Map<String,Object> map) {
        String number = map.get("number").toString();
        DesignOrder designOrder = designOrderDao.findByNumber(number);
        // 还需要更新物资单的状态
        List<UserOrder> list = userOrderDao.findByDesignStatus(designOrder.getStartId(),UserOrderStatus.GOING.getCode());
        for (UserOrder dd : list) {
            userOrderDao.updateStatusByNumber(UserOrderStatus.FINAL_RECEIVE.getCode(),dd.getNumber());
        }
        // 更新调配但的状态
        designOrderDao.updateStatusByNumber(UserOrderStatus.FINAL_RECEIVE.getCode(), number);
        // 更新这个接收站的物资情况
        int stationId = designOrder.getEndId();
        // 获取这个单的资源列表
        int a1 = TypeUtil.getNumber(designOrderDao.getResource(number,ResourceType.N95.getCode()));
        int a2 = TypeUtil.getNumber(designOrderDao.getResource(number,ResourceType.PM25.getCode()));
        int a3 = TypeUtil.getNumber(designOrderDao.getResource(number,ResourceType.Ori.getCode()));

        int aa1 = TypeUtil.getNumber(receiveStationDao.getAmount(stationId,ResourceType.N95.getCode()));
        int aa2 = TypeUtil.getNumber(receiveStationDao.getAmount(stationId,ResourceType.PM25.getCode()));
        int aa3 = TypeUtil.getNumber(receiveStationDao.getAmount(stationId,ResourceType.Ori.getCode()));
        receiveStationDao.updateResource(stationId,ResourceType.N95.getCode(),a1+aa1);
        receiveStationDao.updateResource(stationId,ResourceType.PM25.getCode(),a2+aa2);
        receiveStationDao.updateResource(stationId,ResourceType.Ori.getCode(),a3+aa3);

        log.info(">>>调配单确认 接收站已确认收到");
        return ResultVOUtil.success();
    }

    /**
     * 调配站确认已发出物资
     */
    @ResponseBody
    @PostMapping("/design_verify")
    @ApiOperation(value = "调配站确认已发出物资")
    public ResultVO designVerify(@RequestBody(required = true)Map<String,Object> map) {
        String token = map.get("token").toString();
        DesignStationManager user = designStationManagerDao.findByToken(token);
        if (user == null) {
            log.info(">>>调配站确认已发出物资 不是负责人"+token);
            return ResultVOUtil.error(ExceptionType.AUTHORITY_ERROR.getCode(),"您还不是调配站负责人，无法操作");
        }
        String number = map.get("number").toString();
        // 还需要更新物资单状态
        // 先找到本调配站所有处于待分配状态的物资单,然后遍历修改
        List<UserOrder> list = userOrderDao.findByDesignStatus(user.getStationId(),UserOrderStatus.DE_RECEIVE.getCode());
        for (UserOrder dd : list) {
            userOrderDao.updateStatusByNumber(UserOrderStatus.GOING.getCode(),dd.getNumber());
        }
        // 更新物资状态
        designOrderDao.updateStatusByNumber(UserOrderStatus.GOING.getCode(), number);
        log.info(">>>调配单确认 调配站已确认发出");
        return ResultVOUtil.success();
    }

    @ResponseBody
    @GetMapping("/resource_detail")
    @ApiOperation(value = "获取调配单资源信息")
    public ResultVO getOrderRes(@RequestParam("number")String number) {
        StringBuilder sb = new StringBuilder();
        sb.append("该调配单共有")
                .append(ResourceType.N95.getMsg()).append(TypeUtil.getNumber(designOrderDao.getResource(number,ResourceType.N95.getCode()))).append("个; ")
                .append(ResourceType.PM25.getMsg()).append(TypeUtil.getNumber(designOrderDao.getResource(number,ResourceType.PM25.getCode()))).append("个; ")
                .append(ResourceType.Ori.getMsg()).append(TypeUtil.getNumber(designOrderDao.getResource(number,ResourceType.Ori.getCode()))).append("个; ");
        log.info(">>>获取调配单信息成功");
        return ResultVOUtil.success(sb.toString());
    }

    @ResponseBody
    @PostMapping("/list")
    @ApiOperation(value = "调配站的调配单列表")
    public ResultVO getOrderList(@RequestBody(required = false)Map<String, Object> map) {
        String token = map.get("token").toString();
        Integer status = Integer.valueOf(map.get("status").toString());
        DesignStationManager user = designStationManagerDao.findByToken(token);
        if (user == null) {
            log.info(">>>调配站的调配单列表 不是负责人"+token);
            return ResultVOUtil.error(ExceptionType.AUTHORITY_ERROR.getCode(),"您还不是调配站负责人，无法获取");
        }
        int stationId = user.getStationId();
        List<DesignOrder> row = new ArrayList<>();
        // 全部
        if (status == 0) {
            row = designOrderDao.findByDesign(stationId);
        }
        else {
            row = designOrderDao.findByDesignStatus(stationId,status);
        }
        log.info(">>>调配列表 获取成功");
        return ResultVOUtil.success(row);
    }

    @ResponseBody
    @PostMapping("/list_receive")
    @ApiOperation(value = "接收站的调配单列表")
    public ResultVO getOrderListReceive(@RequestBody(required = false)Map<String, Object> map) {
        String token = map.get("token").toString();
        Integer status = Integer.valueOf(map.get("status").toString());
        ReceiveStationManager user = receiveStationManagerDao.findByToken(token);
        if (user == null) {
            log.info(">>>接收站的调配单列表 不是负责人"+token);
            return ResultVOUtil.error(ExceptionType.AUTHORITY_ERROR.getCode(),"您还不是接收站负责人，无法获取");
        }
        int stationId = user.getStationId();
        List<DesignOrder> row = new ArrayList<>();
        // 全部
        if (status == 0) {
            row = designOrderDao.findByReceive(stationId);
        }
        else {
            row = designOrderDao.findByReceiveStatus(stationId,status);
        }
        log.info(">>>接收站的调配单列表 获取成功");
        return ResultVOUtil.success(row);
    }

}

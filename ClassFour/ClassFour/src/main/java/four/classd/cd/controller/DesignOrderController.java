package four.classd.cd.controller;

import four.classd.cd.annotation.Authorize;
import four.classd.cd.config.SwaggerConfig;
import four.classd.cd.constant.RoleConstant;
import four.classd.cd.dao.*;
import four.classd.cd.model.entity.*;
import four.classd.cd.model.enums.ExceptionType;
import four.classd.cd.model.enums.OrderStatus;
import four.classd.cd.model.vo.ResultVO;
import four.classd.cd.service.StationService;
import four.classd.cd.util.KeyUtil;
import four.classd.cd.util.ResultVOUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    private ReceiveStationManagerDao receiveStationManagerDao;

    @Autowired
    private ResourceDao resourceDao;

    @Autowired
    private StationService stationService;

    @ResponseBody
    @GetMapping("/advice")
    @ApiOperation(value = "获取调配建议")
    @Authorize(role = "1")
    public ResultVO getAdvice(@RequestParam(value = "did",required = false)Integer did) {
        String token = "";
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
    @Authorize(role = "1")
    public ResultVO createDesignOrder(@RequestBody(required = true)Map<String,Object> map) {
        /* 订单参数 */
        String goManagerName = map.get("go_manager_name").toString();
        String goManagerPhone = map.get("go_manager_phone").toString();
        int designStationId = Integer.parseInt(map.get("design_station_id").toString());
        int receiveStationId = Integer.parseInt(map.get("receive_station_id").toString());
        String startDate = map.get("start_date").toString();
        String endDate = map.get("end_date").toString();
        // 资源
        List<DesignOrderResource> resList = (List<DesignOrderResource>)map.get("res_list");
        if (resList.size() < 1) {
            log.info(">>>调配单创建 资源为空");
            return ResultVOUtil.error(ExceptionType.PARAM_ERROR.getCode(), "资源为空");
        }
        int totalAmount = 0;
        int totalWeight = 0;
        for (DesignOrderResource dod : resList) {
            designOrderDao.addResource(dod);
            totalAmount += dod.getAmount();
            totalWeight += dod.getAmount() * resourceDao.getWeight(dod.getTypeCode());
        }
        // todo 从token获取designID
        DesignStation designStation = designStationDao.findById(designStationId);
        ReceiveStation receiveStation = receiveStationDao.findById(receiveStationId);
        ReceiveStationManager receiveStationManager = receiveStationManagerDao.findByStation(receiveStationId);

        DesignOrder designOrder = new DesignOrder();
        designOrder.setId(KeyUtil.generateOrdID());
        designOrder.setNumber(KeyUtil.generateNumber());
        designOrder.setStatus(OrderStatus.GOING.getCode());
        // designOrder.setDesignManagerId();
        // designOrder.setDesignManagerName();
        // designOrder.setDesignManagerPhone();
        designOrder.setGoManagerName(goManagerName); // 运输人的名字
        designOrder.setGoManagerPhone(goManagerPhone); // 运输人的电话
        designOrder.setReceiveManagerId(receiveStationManager.getId());
        designOrder.setReceiveManagerName(receiveStationManager.getUsername());
        designOrder.setReceiveManagerPhone(receiveStationManager.getPhone());
        designOrder.setTotalWeight(totalWeight);
        designOrder.setTotalAmount(totalAmount);
        // designOrder.setResList();
        designOrder.setStartId(designStationId);
        designOrder.setStartName(designStation.getName());
        designOrder.setStartAddress(designStation.getAddress());
        designOrder.setStartDate(startDate);
        designOrder.setEndId(receiveStationId);
        designOrder.setEndName(receiveStation.getName());
        designOrder.setEndAddress(receiveStation.getAddress());
        designOrder.setEndDate(endDate);
        designOrderDao.addOrder(designOrder);
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
    @Authorize(role = "2")
    public ResultVO userVerify(@RequestBody(required = true)Map<String,Object> map) {
        String number = map.get("number").toString();
        designOrderDao.updateStatusByNumber(OrderStatus.FINAL_RECEIVE.getCode(), number);
        log.info(">>>调配单确认 接收站已确认收到");
        return ResultVOUtil.success();
    }

    /**
     * 调配站确认已发出物资
     */
    @ResponseBody
    @PostMapping("/design_verify")
    @ApiOperation(value = "调配站确认已发出物资")
    @Authorize(role = "1")
    public ResultVO designVerify(@RequestBody(required = true)Map<String,Object> map) {
        String number = map.get("number").toString();
        designOrderDao.updateStatusByNumber(OrderStatus.GOING.getCode(), number);
        log.info(">>>调配单确认 调配站已确认发出");
        return ResultVOUtil.success();
    }

}

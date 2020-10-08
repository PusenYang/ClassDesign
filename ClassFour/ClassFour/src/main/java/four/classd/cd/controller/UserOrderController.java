package four.classd.cd.controller;

import four.classd.cd.annotation.Authorize;
import four.classd.cd.config.SwaggerConfig;
import four.classd.cd.dao.*;
import four.classd.cd.model.entity.*;
import four.classd.cd.model.enums.ExceptionType;
import four.classd.cd.model.enums.ResourceType;
import four.classd.cd.model.enums.UserOrderStatus;
import four.classd.cd.model.vo.ResultVO;
import four.classd.cd.service.OrderService;
import four.classd.cd.util.DateUtil;
import four.classd.cd.util.KeyUtil;
import four.classd.cd.util.ResultVOUtil;
import four.classd.cd.util.TypeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author PusenYang
 * @version 1.0
 * @date 2020/10/3 9:34
 */
@RestController
@RequestMapping("/u_order")
@Slf4j
@Api(tags = SwaggerConfig.ORDER_TAG)
@CrossOrigin(origins = "*")
public class UserOrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ResourceDao resourceDao;

    @Autowired
    private UserOrderDao userOrderDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private DesignStationDao designStationDao;

    @Autowired
    private DesignStationManagerDao designStationManagerDao;

    @ResponseBody
    @PostMapping("/create")
    @ApiOperation(value = "用户创建物资单")
    @Authorize(role = "3")
    public ResultVO createUserOrder(@RequestBody(required = true)Map<String,Object> map) {
        /* 订单参数 */
        // todo 从token获取USERID
        String token = "";
        int userId = userDao.findIdByToken(token);
        String phone = map.get("phone").toString();
        String address = map.get("address").toString();
        String date = map.get("date").toString();
        int designId = Integer.parseInt(map.get("design_id").toString());
        DesignStation station = designStationDao.findById(designId);
        // 获取资源列表
        List<UserOrderResource> resList = (List<UserOrderResource>) map.get("res_list");
        if (resList.size() < 1) {
            log.info(">>>物资单 资源列表为空");
            return ResultVOUtil.error(ExceptionType.PARAM_ERROR.getCode(), "资源列表为空");
        }
        // 遍历列表
        int totalAmount = 0;
        int totalWeight = 0;
        for (UserOrderResource uod : resList) {
            userOrderDao.addResource(uod);
            totalAmount += uod.getAmount();
            totalWeight += uod.getAmount() * resourceDao.getWeight(uod.getType().getCode());
        }

        UserOrder order = new UserOrder();
        order.setId(KeyUtil.generateOrdID());
        order.setNumber(KeyUtil.generateNumber());
        order.setStatus(UserOrderStatus.NEW.getCode()); // 刚创建
        order.setUserId(userId);
        order.setPhone(phone);
        order.setAddress(address);
        order.setResList(resList);
        order.setTotalWeight(totalWeight);
        order.setTotalAmount(totalAmount);
        order.setTheDate(date);
        order.setDesignId(designId);
        order.setDesignName(station.getName());
        order.setDesignAddress(station.getAddress());
        orderService.createUserOrder(order);

        log.info(">>>物资单 创建成功");
        return ResultVOUtil.success();
    }

    @ResponseBody
    @PostMapping("/user_verify")
    @ApiOperation(value = "用户确认物资已发出")
    public ResultVO userVerify(@RequestBody(required = true)Map<String,Object> map) {
        String token = map.get("token").toString();
        String number = map.get("number").toString();
        User user = userDao.findByToken(token);
        if (user == null) {
            log.info(">>>确认物资已发出 失败"+token);
            return ResultVOUtil.error(ExceptionType.AUTHORITY_ERROR.getCode(),"您还不是个人用户，无法操作");
        }
        userOrderDao.updateStatusByNumber(UserOrderStatus.DE_UNRECEIVE.getCode(), number);
        userOrderDao.updateTheDateByNumber(number, DateUtil.getTimeNow());
        log.info(">>>物资单确认 用户已确认发出");
        return ResultVOUtil.success();
    }

    @ResponseBody
    @PostMapping("/design_verify")
    @ApiOperation(value = "调配站管理人员确认物资已收到")
    public ResultVO designVerify(@RequestBody(required = true)Map<String,Object> map) {
        String number = map.get("number").toString();
        userOrderDao.updateStatusByNumber(UserOrderStatus.DE_RECEIVE.getCode(), number);
        // 对应调配站增加对应物资
        UserOrder order = userOrderDao.findByNumber(number);
        // 获取这个单的资源列表
        int a1 = TypeUtil.getNumber(userOrderDao.getResource(number,ResourceType.N95.getCode()));
        int a2 = TypeUtil.getNumber(userOrderDao.getResource(number,ResourceType.PM25.getCode()));
        int a3 = TypeUtil.getNumber(userOrderDao.getResource(number,ResourceType.Ori.getCode()));
        // 更新站点物资
        int stationId = order.getDesignId();
        designStationDao.updateResource(stationId,ResourceType.N95.getCode(),a1);
        designStationDao.updateResource(stationId,ResourceType.PM25.getCode(),a2);
        designStationDao.updateResource(stationId,ResourceType.Ori.getCode(),a3);
        // 更新单状态
        userOrderDao.updateStatusByNumber(UserOrderStatus.DE_RECEIVE.getCode(),number);
        // 更新日期
        userOrderDao.updateDesignDateByNumber(number, DateUtil.getTimeNow());

        log.info(">>>物资单确认 调配站已确认收到");
        return ResultVOUtil.success();
    }

    @ResponseBody
    @PostMapping("/list_design")
    @ApiOperation(value = "调配站的物资单列表")
    public ResultVO getOrderListOfDesign(@RequestBody(required = false)Map<String, Object> map) {
        String token = map.get("token").toString();
        Integer status = Integer.parseInt(map.get("status").toString());
        DesignStationManager dsm = designStationManagerDao.findByToken(token);
        if (dsm == null) {
            log.info(">>>调配站的物资单列表 不是负责人"+token);
            return ResultVOUtil.error(ExceptionType.AUTHORITY_ERROR.getCode(),"您还不是站点负责人，无法获取");
        }
        int stationId = dsm.getStationId();
        List<UserOrder> row = new ArrayList<>();
        // 全部
        if (status == 0) {
            row = userOrderDao.findByDesign(stationId);
        }
        else {
            row = userOrderDao.findByDesignStatus(stationId, status);
        }
        log.info(">>>调配站的物资单列表 获取成功");
        return ResultVOUtil.success(row);
    }

    @ResponseBody
    @PostMapping("/list")
    @ApiOperation(value = "我的物资单列表")
    public ResultVO getOrderList(@RequestBody(required = false)Map<String, Object> map) {
        String token = map.get("token").toString();
        String province = map.get("province").toString();
        Integer status = Integer.valueOf(map.get("status").toString());
        User user = userDao.findByToken(token);
        if (user == null) {
            log.info(">>>我的物资单列表 个人用户"+token);
            return ResultVOUtil.error(ExceptionType.AUTHORITY_ERROR.getCode(),"您还不是个人用户，无法获取");
        }
        int userId = user.getId();
        List<UserOrder> row = new ArrayList<>();
        // 全部
        if (status == 0 && province.equals("0")) {
            row = userOrderDao.findByUser(userId);
        }
        // 只有省份筛选
        if (status == 0 && ! province.equals("0")) {
            row = userOrderDao.findByUserProvince(userId,province);
        }
        // 只有状态筛选
        if (status != 0 && province.equals("0")) {
            row = userOrderDao.findByUserStatus(userId,status);
        }
        // 都筛选
        if (status != 0 && !province.equals("0")) {
            row = userOrderDao.findByUserProvinceStatus(userId,province,status);
        }
        log.info(">>>物资单列表 获取成功");
        return ResultVOUtil.success(row);
    }

    @ResponseBody
    @GetMapping("/date")
    @ApiOperation(value = "物资单的时间信息")
    public ResultVO getDate(@RequestParam(value = "number")String number) {
        StringBuilder sb = new StringBuilder();
        sb.append("物资发送日期：").append(TypeUtil.getDate(userOrderDao.getTheDate(number))).append(";        ")
          .append("调配站收取日期：").append(TypeUtil.getDate(userOrderDao.getDesignDate(number))).append(";        ")
          .append("接收站确认日期：").append(TypeUtil.getDate(userOrderDao.getReceiveDate(number))).append(";");
        log.info(">>>获取物资单时间信息成功");
        return ResultVOUtil.success(sb.toString());
    }

    @ResponseBody
    @GetMapping("/resource_detail")
    @ApiOperation(value = "获取物资单资源信息")
    public ResultVO getOrderRes(@RequestParam("number")String number) {
        StringBuilder sb = new StringBuilder();
        sb.append("该物资单共有")
          .append(ResourceType.N95.getMsg()).append(TypeUtil.getNumber(userOrderDao.getResource(number,ResourceType.N95.getCode()))).append("个; ")
          .append(ResourceType.PM25.getMsg()).append(TypeUtil.getNumber(userOrderDao.getResource(number,ResourceType.PM25.getCode()))).append("个; ")
          .append(ResourceType.Ori.getMsg()).append(TypeUtil.getNumber(userOrderDao.getResource(number,ResourceType.Ori.getCode()))).append("个; ");
        log.info(">>>获取物资单资源信息成功");
        return ResultVOUtil.success(sb.toString());
    }

    @ResponseBody
    @GetMapping("/info")
    @ApiOperation(value = "物资单信息")
    @Authorize(role = "3")
    public ResultVO getOrderInfo(@RequestParam("number")String number) {
        UserOrder order = userOrderDao.findByNumber(number);
        log.info(">>>物资单信息， 获取成功");
        return ResultVOUtil.success(order);
    }

}

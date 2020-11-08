package four.classd.cd.controller;

import com.github.pagehelper.util.StringUtil;
import four.classd.cd.config.SwaggerConfig;
import four.classd.cd.constant.PathConstant;
import four.classd.cd.constant.RoleAvatar;
import four.classd.cd.dao.*;
import four.classd.cd.model.entity.*;
import four.classd.cd.model.enums.ExceptionType;
import four.classd.cd.model.enums.ResourceType;
import four.classd.cd.model.vo.ResultVO;
import four.classd.cd.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @author PusenYang
 * @version 1.0
 * @date 2020/10/1 21:22
 */
@RestController
@RequestMapping("/register")
@Slf4j
@Api(tags = SwaggerConfig.LOGIN_TAG)
@CrossOrigin(origins = "*")
public class RegisterController {
    @Autowired
    private UserDao userDao;

    @Autowired
    private AdminDao adminDao;

    @Autowired
    private DesignStationDao designStationDao;

    @Autowired
    private ReceiveStationDao receiveStationDao;

    @Autowired
    private DesignStationManagerDao designStationManagerDao;

    @Autowired
    private ReceiveStationManagerDao receiveStationManagerDao;

    @PostMapping("/image")
    @ResponseBody
    @ApiOperation(value = "图片上传接口")
    public ResultVO uploadImage(@RequestParam("file") MultipartFile file) {
        String localName = file.getOriginalFilename(); // 上传文件的真实名称
        log.info(">>> 文件的真实名称是: " + localName);
        String suffixName = localName.substring(localName.lastIndexOf(".")).toLowerCase();//获取后缀名
        if (!suffixName.equals(".png") && !suffixName.equals(".jpg") && !suffixName.equals(".jpeg") && !suffixName.equals(".bmp") && !suffixName.equals(".tif")) {
            log.info(">>>图片上传 不支持的格式："+suffixName);
            return ResultVOUtil.error(ExceptionType.PARAM_ERROR.getCode(), "不支持文件的格式");
        }
        String savePath = PathConstant.SERVER + KeyUtil.generateUserID() + localName;
        log.info(">>> 文件的存储路径是: " + savePath);
        try {
            file.transferTo(new File(savePath));
        } catch (IOException e) {
            log.info(">>>图片上传 发生错误："+e.toString());
            return ResultVOUtil.error(ExceptionType.SERVER_ERROR.getCode(), "图片上传发生错误，请稍后重试");
        }
        log.info(">>>图片上传 成功 : " + savePath);
        return ResultVOUtil.success(savePath);
    }

    @PostMapping("/user")
    @ResponseBody
    @ApiOperation(value = "普通用户注册")
    public ResultVO userRegister(@RequestBody(required = false)Map<String, Object> map) {
        /* 参数获取 */
        String username = map.get("username").toString();
        String password = map.get("password").toString();
        String password2 = map.get("password2").toString();
        String phone = map.get("phone").toString();
        String address = map.get("address").toString();
        String avatar = RoleAvatar.USER_AVATAR;

        /* 验证 */
        if (StringUtil.isEmpty(username) || StringUtil.isEmpty(password)) {
            log.info(">>>注册 用户名或密码为空");
            return ResultVOUtil.error(ExceptionType.PARAM_ERROR.getCode(), "用户名或密码为空");
        }
        if (StringUtil.isEmpty(phone)) {
            log.info(">>>注册 手机号码为空");
            return ResultVOUtil.error(ExceptionType.PARAM_ERROR.getCode(), "手机号码为空");
        }
        if (! password.equals(password2)) {
            log.info(">>>注册 两次输入的密码不一致");
            return ResultVOUtil.error(ExceptionType.PARAM_ERROR.getCode(), "两次输入的密码不一致");
        }
        if (userDao.findByName(username) != null) {
            log.info(">>>注册 该用户名已被占用");
            return ResultVOUtil.error(ExceptionType.PARAM_ERROR.getCode(), "该用户名已被占用");
        }

        String salt = KeyUtil.generateSalt();
        User user = new User();
        user.setId(KeyUtil.generateOrdID());
        user.setUsername(username);
        user.setAvatar(avatar);
        user.setPassword(MD5Util.MD5(salt+password));
        user.setSalt(salt);
        user.setPhone(phone);
        user.setAddress(address);
        user.setCreateTime(DateUtil.getTimeNow());
        try {
            String[] arr = GaodeMapUtil.getLocation(address);
            user.setLongitude(Double.valueOf(arr[0]));
            user.setLatitude(Double.valueOf(arr[1]));
        } catch (IOException e) {
            log.info(">>>用户注册 地址解析出现错误");
            return ResultVOUtil.error(ExceptionType.SERVER_ERROR.getCode(),"地址解析出现错误, 请检查您输入的地址");
        }
        userDao.addUser(user);

        log.info(">>>注册 普通用户注册成功");
        return ResultVOUtil.success();
    }

    /**
     * 调配站管理人员注册, 同时增加相应调配站
     * @param map
     * @return
     */
    @PostMapping("/design_manager")
    @ResponseBody
    @ApiOperation(value = "调配站管理人员注册")
    public ResultVO designManagerRegister(@RequestBody(required = false)Map<String, Object> map) {
        /* 参数获取 */
        String username = map.get("username").toString();
        String password = map.get("password").toString();
        String password2 = map.get("password2").toString();
        String phone = map.get("phone").toString();
        String avatar = RoleAvatar.DESIGN_AVATAR;
        String idCard = map.get("id_card").toString();

        String stationName = map.get("station_name").toString();
        String stationAddress = map.get("station_address").toString();
        String stationCounty = map.get("station_county").toString();
        String stationProvince = map.get("station_province").toString();
        String stationCity = map.get("station_city").toString();
        String stationImage = map.get("station_image").toString();
        String stationRemark = map.get("station_remark").toString();
        log.info(">>> 调配站管理人员注册, 接收到的图片链接是: " + stationImage);
        /* 验证 */
        if (StringUtil.isEmpty(username) || StringUtil.isEmpty(password)) {
            log.info(">>>注册 用户名或密码为空");
            return ResultVOUtil.error(ExceptionType.PARAM_ERROR.getCode(), "用户名或密码为空");
        }
        if (StringUtil.isEmpty(idCard)) {
            log.info(">>>注册 身份证id为空");
            return ResultVOUtil.error(ExceptionType.PARAM_ERROR.getCode(), "身份证id为空");
        }
        if (StringUtil.isEmpty(stationName)) {
            log.info(">>>注册 调配站站点名字为空");
            return ResultVOUtil.error(ExceptionType.PARAM_ERROR.getCode(), "调配站站点名字为空");
        }
        if (StringUtil.isEmpty(phone)) {
            log.info(">>>注册 手机号码为空");
            return ResultVOUtil.error(ExceptionType.PARAM_ERROR.getCode(), "手机号码为空");
        }
        if (StringUtil.isEmpty(stationImage)) {
            log.info(">>>注册 调配站资格凭证为空");
            return ResultVOUtil.error(ExceptionType.PARAM_ERROR.getCode(), "调配站资格凭证为空");
        }
        if (! password.equals(password2)) {
            log.info(">>>注册 两次输入的密码不一致");
            return ResultVOUtil.error(ExceptionType.PARAM_ERROR.getCode(), "两次输入的密码不一致");
        }
        if (designStationManagerDao.findByName(username) != null) {
            log.info(">>>注册 该用户名已被占用");
            return ResultVOUtil.error(ExceptionType.PARAM_ERROR.getCode(), "该用户名已被占用");
        }

        /* 添加站点 */
        DesignStation station = new DesignStation();
        int stationId = KeyUtil.generateOrdID();
        int managerId = KeyUtil.generateOrdID();
        System.out.println(stationName);
        System.out.println(stationAddress);
        station.setId(stationId);
        station.setName(stationName);
        DesignStation rs = designStationDao.findByName(stationName);
        if (rs != null) {
            log.info(">>>调配站站注册 名字不唯一");
            return ResultVOUtil.error(ExceptionType.PARAM_ERROR.getCode(), "该站点名字已被注册");
        }
        station.setAddress(stationAddress);
        station.setCounty(stationCounty);
        station.setProvince(stationProvince);
        station.setCity(stationCity);
        station.setImage(stationImage);
        station.setRemark(stationRemark);
        station.setManagerId(managerId);
        station.setManagerName(username);
        station.setManagerPhone(phone);
        station.setCreateTime(DateUtil.getTimeNow());
        try {
            String[] arr = GaodeMapUtil.getLocation(stationAddress);
            station.setLongitude(Double.valueOf(arr[0]));
            station.setLatitude(Double.valueOf(arr[1]));
        } catch (IOException e) {
            log.info(">>>调配站注册 地址解析出现错误");
            return ResultVOUtil.error(ExceptionType.SERVER_ERROR.getCode(),"地址解析出现错误, 请检查您输入的地址");
        }
        designStationDao.addStation(station);

        /* 添加管理人员 */
        String salt = KeyUtil.generateSalt();
        DesignStationManager manager = new DesignStationManager();
        manager.setId(managerId);
        manager.setUsername(username);
        manager.setPassword(MD5Util.MD5(salt+password));
        manager.setSalt(salt);
        manager.setPhone(phone);
        manager.setIdCard(idCard);
        manager.setStationId(stationId);
        manager.setAvatar(avatar);
        manager.setCreateTime(DateUtil.getTimeNow());
        designStationManagerDao.addManager(manager);

        /* 初始化物资 */
        designStationDao.addResource(new DesignStationResource(stationId,ResourceType.N95.getCode(),0));
        designStationDao.addResource(new DesignStationResource(stationId,ResourceType.PM25.getCode(),0));
        designStationDao.addResource(new DesignStationResource(stationId,ResourceType.Ori.getCode(),0));

        log.info(">>>注册 调配站管理人员注册成功");
        return ResultVOUtil.success();
    }

    /**
     * 接收站管理人员注册, 同时增加相应接收站
     * @param map
     * @return
     */
    @PostMapping("/receive_manager")
    @ResponseBody
    @ApiOperation(value = "接收站管理人员注册")
    public ResultVO receiveManagerRegister(@RequestBody(required = false)Map<String, Object> map) {
        /* 参数获取 */
        String username = map.get("username").toString();
        String password = map.get("password").toString();
        String password2 = map.get("password2").toString();
        String phone = map.get("phone").toString();
        String avatar = RoleAvatar.RECEIVE_AVATAR;
        String idCard = map.get("id_card").toString();

        String stationName = map.get("station_name").toString();
        String stationAddress = map.get("station_address").toString();
        String stationProvince = map.get("station_province").toString();
        String stationCity = map.get("station_city").toString();
        String stationImage = map.get("station_image").toString();
        String stationRemark = map.get("station_remark").toString();

        /* 验证 */
        if (StringUtil.isEmpty(username) || StringUtil.isEmpty(password)) {
            log.info(">>>注册 用户名或密码为空");
            return ResultVOUtil.error(ExceptionType.PARAM_ERROR.getCode(), "用户名或密码为空");
        }
        if (StringUtil.isEmpty(idCard)) {
            log.info(">>>注册 身份证id为空");
            return ResultVOUtil.error(ExceptionType.PARAM_ERROR.getCode(), "身份证id为空");
        }
        if (StringUtil.isEmpty(stationName)) {
            log.info(">>>注册 接收站站点名字为空");
            return ResultVOUtil.error(ExceptionType.PARAM_ERROR.getCode(), "接收站站点名字为空");
        }
        if (StringUtil.isEmpty(phone)) {
            log.info(">>>注册 手机号码为空");
            return ResultVOUtil.error(ExceptionType.PARAM_ERROR.getCode(), "手机号码为空");
        }
        if (StringUtil.isEmpty(stationImage)) {
            log.info(">>>注册 调配站资格凭证为空");
            return ResultVOUtil.error(ExceptionType.PARAM_ERROR.getCode(), "调配站资格凭证为空");
        }
        if (! password.equals(password2)) {
            log.info(">>>注册 两次输入的密码不一致");
            return ResultVOUtil.error(ExceptionType.PARAM_ERROR.getCode(), "两次输入的密码不一致");
        }
        if (designStationManagerDao.findByName(username) != null) {
            log.info(">>>注册 该用户名已被占用");
            return ResultVOUtil.error(ExceptionType.PARAM_ERROR.getCode(), "该用户名已被占用");
        }

        /* 添加站点 */
        ReceiveStation station = new ReceiveStation();
        int stationId = KeyUtil.generateOrdID();
        int managerId = KeyUtil.generateOrdID();
        station.setId(stationId);
        station.setName(stationName);
        ReceiveStation rs = receiveStationDao.findByName(stationName);
        if (rs != null) {
            log.info(">>>接收站注册 名字不唯一");
            return ResultVOUtil.error(ExceptionType.PARAM_ERROR.getCode(), "该站点名字已被注册");
        }
        station.setAddress(stationAddress);
        station.setProvince(stationProvince);
        station.setCity(stationCity);
        station.setImage(stationImage);
        station.setRemark(stationRemark);
        station.setManagerId(managerId);
        station.setManagerName(username);
        station.setManagerPhone(phone);
        station.setCreateTime(DateUtil.getTimeNow());
        try {
            String[] arr = GaodeMapUtil.getLocation(stationAddress);
            station.setLongitude(Double.valueOf(arr[0]));
            station.setLatitude(Double.valueOf(arr[1]));
        } catch (IOException e) {
            log.info(">>>接收站注册 地址解析出现错误");
            return ResultVOUtil.error(ExceptionType.SERVER_ERROR.getCode(),"地址解析出现错误, 请检查您输入的地址");
        }
        receiveStationDao.addStation(station);

        /* 添加管理人员 */
        String salt = KeyUtil.generateSalt();
        ReceiveStationManager manager = new ReceiveStationManager();
        manager.setId(managerId);
        manager.setUsername(username);
        manager.setPassword(MD5Util.MD5(salt+password));
        manager.setSalt(salt);
        manager.setPhone(phone);
        manager.setIdCard(idCard);
        manager.setStationId(stationId);
        manager.setAvatar(avatar);
        manager.setCreateTime(DateUtil.getTimeNow());
        receiveStationManagerDao.addManager(manager);

        /* 初始化物资 */
        receiveStationDao.addResource(new ReceiveStationResource(stationId,ResourceType.N95.getCode(),0));
        receiveStationDao.addResource(new ReceiveStationResource(stationId,ResourceType.PM25.getCode(),0));
        receiveStationDao.addResource(new ReceiveStationResource(stationId,ResourceType.Ori.getCode(),0));

        log.info(">>>注册 调配站管理人员注册成功");
        return ResultVOUtil.success();
    }

}

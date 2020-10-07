package four.classd.cd.controller;

import four.classd.cd.annotation.Authorize;
import four.classd.cd.config.SwaggerConfig;
import four.classd.cd.dao.*;
import four.classd.cd.model.entity.*;
import four.classd.cd.model.enums.ExceptionType;
import four.classd.cd.model.vo.ResultVO;
import four.classd.cd.util.ResultVOUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/info")
@Slf4j
@Api(tags = SwaggerConfig.INFO_TAG)
@CrossOrigin(origins = "*")
public class InfoController {

    @Autowired
    private PositionDao positionDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private DesignStationManagerDao designStationManagerDao;

    @Autowired
    private ReceiveStationManagerDao receiveStationManagerDao;

    @Autowired
    private DesignStationDao designStationDao;

    @Autowired
    private ReceiveStationDao receiveStationDao;

    @ResponseBody
    @GetMapping("/d_station")
    @ApiOperation(value = "获取调配站信息")
    @Authorize(role = "1")
    public ResultVO getDesignStation(@RequestParam("token")String token) {
        DesignStationManager dsm = designStationManagerDao.findByToken(token);
        DesignStation ds = designStationDao.findById(dsm.getStationId());
        if (ds == null) {
            log.info(">>>获取调配站信息 未查到："+token);
            return ResultVOUtil.error(ExceptionType.INFO_NULL.getCode(), "未查到该站点");
        }
        List<String> list = Stream.of(ds.getName(),ds.getAddress(),ds.getImage(),ds.getManagerName(),ds.getManagerPhone()).collect(toList());
        log.info(">>>获取调配站信息 成功"+token);
        return ResultVOUtil.success(list);
    }

    @ResponseBody
    @GetMapping("/r_station")
    @ApiOperation(value = "获取接收站信息")
    @Authorize(role = "2")
    public ResultVO getReceiveStation(@RequestParam("token")String token) {
        ReceiveStationManager dsm = receiveStationManagerDao.findByToken(token);
        ReceiveStation ds = receiveStationDao.findById(dsm.getStationId());
        if (ds == null) {
            log.info(">>>获取接收站信息 未查到："+token);
            return ResultVOUtil.error(ExceptionType.INFO_NULL.getCode(), "未查到该站点");
        }
        List<String> list = Stream.of(ds.getName(),ds.getAddress(),ds.getImage(),ds.getManagerName(),ds.getManagerPhone()).collect(toList());
        log.info(">>>获取接收站信息 成功"+token);
        return ResultVOUtil.success(list);
    }

    @ResponseBody
    @GetMapping("/user")
    @ApiOperation(value = "获取个人用户信息")
    @Authorize(role = "3")
    public ResultVO getUserInfo(@RequestParam("token")String token) {
        User u = userDao.findByToken(token);
        if (u == null) {
            log.info(">>>获取个人用户信息 未查到该用户："+token);
            return ResultVOUtil.error(ExceptionType.INFO_NULL.getCode(), "未查到该用户");
        }
        List<String> list = Stream.of(u.getAvatar(), u.getUsername(), u.getPhone(), u.getAddress()).collect(toList());
        log.info(">>>获取个人用户信息 成功"+token);
        return ResultVOUtil.success(list);
    }

    @ResponseBody
    @GetMapping("/d_manager")
    @ApiOperation(value = "获取调配站负责人信息")
    @Authorize(role = "1")
    public ResultVO getDesignManagerInfo(@RequestParam("token")String token) {
        DesignStationManager u = designStationManagerDao.findByToken(token);
        if (u == null) {
            log.info(">>>获取调配站负责人信息 未查到该用户："+token);
            return ResultVOUtil.error(ExceptionType.INFO_NULL.getCode(), "未查到该用户");
        }
        DesignStation ds = designStationDao.findById(u.getStationId());
        List<String> list = Stream.of(u.getAvatar(), "角色：调配站负责人",u.getUsername(), u.getPhone(), ds.getName(), ds.getAddress()).collect(toList());
        log.info(">>>获取调配站负责人信息 成功"+token);
        return ResultVOUtil.success(list);
    }

    @ResponseBody
    @GetMapping("/r_manager")
    @ApiOperation(value = "获取接收站负责人信息")
    @Authorize(role = "2")
    public ResultVO getReceiveManagerInfo(@RequestParam("token")String token) {
        ReceiveStationManager u = receiveStationManagerDao.findByToken(token);
        if (u == null) {
            log.info(">>>获取接收站负责人信息 未查到该用户："+token);
            return ResultVOUtil.error(ExceptionType.INFO_NULL.getCode(), "未查到该用户");
        }
        ReceiveStation ds = receiveStationDao.findById(u.getStationId());
        List<String> list = Stream.of(u.getAvatar(), "角色：接收站负责人", u.getUsername(), u.getPhone(), ds.getName(), ds.getAddress()).collect(toList());
        log.info(">>>获取接收站负责人信息 成功"+token);
        return ResultVOUtil.success(list);
    }

    @ResponseBody
    @GetMapping("/avatar_name")
    @ApiOperation(value = "获取用户的头像和名称")
    public ResultVO getAvatarUsername(@RequestParam("token")String token) {
        List<String> list = new ArrayList<>();
        String avatar = "";
        String username = "";
        User user = userDao.findByToken(token);
        DesignStationManager dsm = designStationManagerDao.findByToken(token);
        ReceiveStationManager rsm = receiveStationManagerDao.findByToken(token);
        if (user != null) {
            avatar = user.getAvatar();
            username = user.getUsername();
        }
        else if (dsm != null) {
            avatar = dsm.getAvatar();
            username = dsm.getUsername();
        }
        else if (rsm != null) {
            avatar = rsm.getAvatar();
            username = rsm.getUsername();
        }
        else {
            log.info(">>>获取用户的头像和名称 没有该用户："+token);
            return ResultVOUtil.success();
        }
        list.add(avatar);
        list.add(username);
        log.info(">>>获取用户的头像和名称 成功");
        return ResultVOUtil.success(list);
    }

    @ResponseBody
    @GetMapping("/all_province")
    @ApiOperation(value = "获取所有省份")
    public ResultVO getAllProvince() {
        List<String> provinceList = positionDao.findAllProvince();
        log.info(">>>位置 获取所有省份");
        return ResultVOUtil.success(provinceList);
    }

    @ResponseBody
    @GetMapping("/all_position")
    @ApiOperation(value = "测试接口")
    public ResultVO getAll() {
        List<Position> positionList = positionDao.findAll();
        log.info(">>>位置 获取所有");
        return ResultVOUtil.success(positionList);
    }

}
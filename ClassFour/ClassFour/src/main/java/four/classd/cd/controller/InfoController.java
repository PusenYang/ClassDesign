package four.classd.cd.controller;

import four.classd.cd.annotation.Authorize;
import four.classd.cd.config.SwaggerConfig;
import four.classd.cd.constant.RoleConstant;
import four.classd.cd.dao.*;
import four.classd.cd.model.entity.*;
import four.classd.cd.model.enums.ExceptionType;
import four.classd.cd.model.enums.StationStatus;
import four.classd.cd.model.vo.ResultVO;
import four.classd.cd.model.vo.StationMapVO;
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
    @GetMapping("/station_map")
    @ApiOperation(value = "获取站点地图")
    public ResultVO getStationMap() {
        // 先获取所有站点
        List<DesignStation> designStations = designStationDao.findAll(StationStatus.CHECKED.getCode());
        List<ReceiveStation> receiveStations = receiveStationDao.findAll(StationStatus.CHECKED.getCode());
        List<StationMapVO> res = new ArrayList<>();
        for (DesignStation ds : designStations) {
            StationMapVO smv = new StationMapVO();
            smv.setLat(ds.getLatitude());
            smv.setLng(ds.getLongitude());
            smv.setName(ds.getName());
            smv.setType(RoleConstant.DESIGN_MANAGER);
            smv.setAddress(ds.getAddress());
            smv.setManager(ds.getManagerName());
            res.add(smv);
        }
        for (ReceiveStation rs : receiveStations) {
            StationMapVO smvv = new StationMapVO();
            smvv.setLat(rs.getLatitude());
            smvv.setLng(rs.getLongitude());
            smvv.setName(rs.getName());
            smvv.setType(RoleConstant.RECEIVE_MANAGER);
            smvv.setAddress(rs.getAddress());
            smvv.setManager(rs.getManagerName());
            res.add(smvv);
        }
        log.info(">>>站点地图 获取成功");
        return ResultVOUtil.success(res);
    }

    @ResponseBody
    @GetMapping("/d_station")
    @ApiOperation(value = "获取调配站信息")
    public ResultVO getDesignStation(@RequestParam("token")String token) {
        DesignStationManager dsm = designStationManagerDao.findByToken(token);
        DesignStation ds = designStationDao.findById(dsm.getStationId());
        if (ds == null) {
            log.info(">>>获取调配站信息 未查到："+token);
            return ResultVOUtil.error(ExceptionType.INFO_NULL.getCode(), "您不是负责人，所以无法看到信息");
        }
        List<String> list = Stream.of(ds.getName(),ds.getAddress(),ds.getImage(),ds.getManagerName(),ds.getManagerPhone()).collect(toList());
        log.info(">>>获取调配站信息 成功"+token);
        return ResultVOUtil.success(list);
    }

    @ResponseBody
    @GetMapping("/r_station")
    @ApiOperation(value = "获取接收站信息")
    public ResultVO getReceiveStation(@RequestParam("token")String token) {
        ReceiveStationManager dsm = receiveStationManagerDao.findByToken(token);
        ReceiveStation ds = receiveStationDao.findById(dsm.getStationId());
        if (ds == null) {
            log.info(">>>获取接收站信息 未查到："+token);
            return ResultVOUtil.error(ExceptionType.INFO_NULL.getCode(), "您不是负责人，所以无法看到信息");
        }
        List<String> list = Stream.of(ds.getName(),ds.getAddress(),ds.getImage(),ds.getManagerName(),ds.getManagerPhone()).collect(toList());
        log.info(">>>获取接收站信息 成功"+token);
        return ResultVOUtil.success(list);
    }

    /**
     * 个人用户信息只有自己能看到；而站点和站点负责人的信息应该所有人都看到
     * @param token
     * @return
     */
    @ResponseBody
    @GetMapping("/user")
    @ApiOperation(value = "获取个人用户信息")
    @Authorize(role = "3")
    public ResultVO getUserInfo(@RequestParam("token")String token) {
        User u = userDao.findByToken(token);
        if (u == null) {
            log.info(">>>获取个人用户信息 未查到该用户："+token);
            return ResultVOUtil.error(ExceptionType.INFO_NULL.getCode(), "您不是个人用户，所以无法看到信息");
        }
        List<String> list = Stream.of(u.getAvatar(), u.getUsername(), u.getPhone(), u.getAddress()).collect(toList());
        log.info(">>>获取个人用户信息 成功"+token);
        return ResultVOUtil.success(list);
    }

    @ResponseBody
    @GetMapping("/d_manager")
    @ApiOperation(value = "获取调配站负责人信息")
    public ResultVO getDesignManagerInfo(@RequestParam("token")String token) {
        DesignStationManager u = designStationManagerDao.findByToken(token);
        if (u == null) {
            log.info(">>>获取调配站负责人信息 未查到该用户："+token);
            return ResultVOUtil.error(ExceptionType.INFO_NULL.getCode(), "您不是负责人，所以无法看到信息");
        }
        DesignStation ds = designStationDao.findById(u.getStationId());
        List<String> list = Stream.of(u.getAvatar(), "角色：调配站负责人",u.getUsername(), u.getPhone(), ds.getName(), ds.getAddress()).collect(toList());
        log.info(">>>获取调配站负责人信息 成功"+token);
        return ResultVOUtil.success(list);
    }

    @ResponseBody
    @GetMapping("/r_manager")
    @ApiOperation(value = "获取接收站负责人信息")
    public ResultVO getReceiveManagerInfo(@RequestParam("token")String token) {
        ReceiveStationManager u = receiveStationManagerDao.findByToken(token);
        if (u == null) {
            log.info(">>>获取接收站负责人信息 未查到该用户："+token);
            return ResultVOUtil.error(ExceptionType.INFO_NULL.getCode(), "您不是负责人，所以无法看到信息");
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

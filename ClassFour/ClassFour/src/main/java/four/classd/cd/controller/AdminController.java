package four.classd.cd.controller;

import four.classd.cd.annotation.Authorize;
import four.classd.cd.config.SwaggerConfig;
import four.classd.cd.dao.AdminDao;
import four.classd.cd.dao.DesignStationDao;
import four.classd.cd.dao.ReceiveStationDao;
import four.classd.cd.model.entity.Admin;
import four.classd.cd.model.entity.DesignStation;
import four.classd.cd.model.enums.ExceptionType;
import four.classd.cd.model.enums.StationStatus;
import four.classd.cd.model.vo.ResultVO;
import four.classd.cd.service.AdminService;
import four.classd.cd.util.ResultVOUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author PusenYang
 * @version 1.0
 * @date 2020/10/2 18:31
 */
@RestController
@RequestMapping("/admin")
@Slf4j
@Api(tags = {SwaggerConfig.ADMIN_TAG})
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private DesignStationDao designStationDao;

    @Autowired
    private ReceiveStationDao receiveStationDao;

    @Autowired
    private AdminDao adminDao;

    @ResponseBody
    @PostMapping("/part_receive")
    @ApiOperation(value = "获取接收站", notes = "获取已审核的或未审核的接收站列表")
    public ResultVO getCheckReceive(@RequestBody(required = false)Map<String,Object>map) {
        String flag = map.get("flag").toString();
        String token = map.get("token").toString();
        Admin admin = adminDao.findByToken(token);
        if (admin == null) {
            log.info(">>>获取接收站 不是管理员");
            return ResultVOUtil.error(ExceptionType.AUTHORITY_ERROR.getCode(), "您还不是系统管理员，无法进行此操作");
        }
        if (flag.equals(StationStatus.CHECKED.getCode().toString())) {
            log.info(">>>已审核接收站 获取成功");
            return ResultVOUtil.success(receiveStationDao.findByStatus(StationStatus.CHECKED.getCode()));
        }
        else if (flag.equals(StationStatus.UN_CHECK.getCode().toString())){
            log.info(">>>未审核接收站 获取成功");
            return ResultVOUtil.success(receiveStationDao.findByStatus(StationStatus.UN_CHECK.getCode()));
        }
        else {
            log.info(">>>所有接收站 获取成功");
            return ResultVOUtil.success(receiveStationDao.findAlls());
        }
    }

    @ResponseBody
    @PostMapping("/part_design")
    @ApiOperation(value = "获取调配站", notes = "获取已审核的或未审核的调配站列表")
    public ResultVO getCheckDesign(@RequestBody(required = false)Map<String,Object>map) {
        String flag = map.get("flag").toString();
        String token = map.get("token").toString();
        Admin admin = adminDao.findByToken(token);
        if (admin == null) {
            log.info(">>>获取调配站 不是管理员");
            return ResultVOUtil.error(ExceptionType.AUTHORITY_ERROR.getCode(), "您还不是系统管理员，无法进行此操作");
        }
        if (flag.equals(StationStatus.CHECKED.getCode().toString())) {
            log.info(">>>已审核调配站 获取成功");
            return ResultVOUtil.success(designStationDao.findByStatus(StationStatus.CHECKED.getCode()));
        }
        else if (flag.equals(StationStatus.UN_CHECK.getCode().toString())){
            log.info(">>>未审核调配站 获取成功");
            return ResultVOUtil.success(designStationDao.findByStatus(StationStatus.UN_CHECK.getCode()));
        }
        else {
            log.info(">>>所有调配站 获取成功");
            return ResultVOUtil.success(designStationDao.findAlls());
        }
    }

    @ResponseBody
    @PostMapping("/do_check")
    @ApiOperation(value = "审核站点", notes = "")
    public ResultVO checkDesign(@RequestBody(required = false)Map<String,Object>map) {
        String token = map.get("token").toString();
        Admin admin = adminDao.findByToken(token);
        if (admin == null) {
            log.info(">>>审核站点 不是管理员");
            return ResultVOUtil.error(ExceptionType.AUTHORITY_ERROR.getCode(), "您还不是系统管理员，无法进行此操作");
        }
        int id = Integer.parseInt(map.get("id").toString());
        String name = map.get("name").toString();
        int verify = Integer.parseInt(map.get("verify").toString());
        DesignStation ds = designStationDao.findByIdAndName(id,name);
        // 是调配站
        if (ds != null) {
            if (verify == 1) adminService.checkDesign(StationStatus.CHECKED.getCode(),id);
            else adminService.checkDesign(StationStatus.UN_CHECK.getCode(), id);
        }
        else {
            if (verify == 1) adminService.checkReceive(StationStatus.CHECKED.getCode(),id);
            else adminService.checkReceive(StationStatus.UN_CHECK.getCode(), id);
        }

        log.info(">>>审核完成"+id);
        return ResultVOUtil.success();
    }

}

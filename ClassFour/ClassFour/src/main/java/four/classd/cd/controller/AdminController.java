package four.classd.cd.controller;

import four.classd.cd.annotation.Authorize;
import four.classd.cd.config.SwaggerConfig;
import four.classd.cd.dao.AdminDao;
import four.classd.cd.dao.DesignStationDao;
import four.classd.cd.dao.ReceiveStationDao;
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
    @GetMapping("/part_receive")
    @ApiOperation(value = "获取接收站", notes = "获取已审核的或未审核的接收站列表")
    @Authorize(role = "0")
    public ResultVO getCheckReceive(@RequestParam("flag")String flag) {
        if (flag.equals(StationStatus.CHECKED.getCode())) {
            log.info(">>>已审核接收站 获取成功");
            return ResultVOUtil.success(receiveStationDao.findByStatus(StationStatus.CHECKED.getCode()));
        }
        else if (flag.equals(StationStatus.UN_CHECK.getCode())){
            log.info(">>>未审核接收站 获取成功");
            return ResultVOUtil.success(receiveStationDao.findByStatus(StationStatus.UN_CHECK.getCode()));
        }
        else {
            log.info(">>>所有接收站 获取成功");
            return ResultVOUtil.success(receiveStationDao.findAlls());
        }
    }

    @ResponseBody
    @GetMapping("/part_design")
    @ApiOperation(value = "获取调配站", notes = "获取已审核的或未审核的调配站列表")
    @Authorize(role = "0")
    public ResultVO getCheckDesign(@RequestParam("flag")String flag) {
        if (flag.equals(StationStatus.CHECKED.getCode())) {
            log.info(">>>已审核调配站 获取成功");
            return ResultVOUtil.success(designStationDao.findByStatus(StationStatus.UN_CHECK.getCode()));
        }
        else if (flag.equals(StationStatus.UN_CHECK.getCode())){
            log.info(">>>未审核调配站 获取成功");
            return ResultVOUtil.success(designStationDao.findByStatus(StationStatus.CHECKED.getCode()));
        }
        else {
            log.info(">>>所有调配站 获取成功");
            return ResultVOUtil.success(designStationDao.findAlls());
        }
    }

    @ResponseBody
    @PostMapping("/check_design")
    @ApiOperation(value = "审核调配站", notes = "")
    @Authorize(role = "0")
    public ResultVO checkDesign(@RequestBody(required = false)Map<String, Object> map) {
        int id = Integer.parseInt(map.get("id").toString());
        Integer status = Integer.parseInt(map.get("status").toString());
        if (adminService.checkDesign(status, id)) {
            log.info(">>>审核 调配站通过审核: "+id);
            return ResultVOUtil.success();
        }
        else {
            log.info(">>>审核 调配站未通过审核: "+id);
            return ResultVOUtil.success();
        }
    }

    @ResponseBody
    @PostMapping("/check_receive")
    @ApiOperation(value = "审核接收站", notes = "")
    @Authorize(role = "0")
    public ResultVO checkReceive(@RequestBody(required = false)Map<String, Object> map) {
        int id = Integer.parseInt(map.get("id").toString());
        Integer status = Integer.parseInt(map.get("status").toString());
        if (adminService.checkReceive(status, id)) {
            log.info(">>>审核 接收站通过审核: "+id);
            return ResultVOUtil.success();
        }
        else {
            log.info(">>>审核 接收站未通过审核: "+id);
            return ResultVOUtil.success();
        }
    }

}

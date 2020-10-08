package four.classd.cd.controller;

import com.github.pagehelper.util.StringUtil;
import four.classd.cd.config.SwaggerConfig;
import four.classd.cd.dao.ReceiveStationDao;
import four.classd.cd.dao.ReceiveStationManagerDao;
import four.classd.cd.dao.ResourceDao;
import four.classd.cd.model.entity.ReceiveStation;
import four.classd.cd.model.entity.ReceiveStationManager;
import four.classd.cd.model.enums.ExceptionType;
import four.classd.cd.model.enums.ResourceType;
import four.classd.cd.model.enums.StationStatus;
import four.classd.cd.model.vo.ResultVO;
import four.classd.cd.model.vo.StationResourceVO;
import four.classd.cd.util.ResultVOUtil;
import four.classd.cd.util.TypeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/r_station")
@Slf4j
@CrossOrigin(origins = "*")
@Api(tags = SwaggerConfig.RECEIVE_TAG)
public class ReceiveStationController {

    @Autowired
    private ReceiveStationDao receiveStationDao;

    @Autowired
    private ReceiveStationManagerDao receiveStationManagerDao;

    @Autowired
    private ResourceDao resourceDao;

    @ResponseBody
    @PostMapping("/resource_list_edit")
    @ApiOperation(value = "接收站物资清点-修改")
    public ResultVO editResource(@RequestBody() Map<String, Object> map) {
        String token = map.get("token").toString();
        ReceiveStationManager dsm = receiveStationManagerDao.findByToken(token);
        if (dsm == null) {
            log.info(">>>接收站物资清点-修改 不是负责人"+token);
            return ResultVOUtil.error(ExceptionType.AUTHORITY_ERROR.getCode(),"您还不是站点负责人，无法进行物资清点");
        }
        String type = map.get("type").toString();
        int stationId = dsm.getStationId();
        int amount = Integer.parseInt(map.get("amount").toString());
        receiveStationDao.updateResource(stationId,ResourceType.getCode(type),amount);
        log.info(">>>接收站物资清点-修改 成功");
        return ResultVOUtil.success();
    }

    @ResponseBody
    @GetMapping("/resource_list")
    @ApiOperation(value = "接收站物资清点-列表")
    public ResultVO getDesignResourceList(@RequestParam("token")String token) {
        ReceiveStationManager dsm = receiveStationManagerDao.findByToken(token);
        if (dsm == null) {
            log.info(">>>接收站物资清点-列表 不是负责人"+token);
            return ResultVOUtil.error(ExceptionType.AUTHORITY_ERROR.getCode(),"您还不是站点负责人，无法进行物资清点");
        }
        int stationId = dsm.getStationId();
        int w1 = resourceDao.getWeight(ResourceType.N95.getCode());
        int w2 = resourceDao.getWeight(ResourceType.PM25.getCode());
        int w3 = resourceDao.getWeight(ResourceType.Ori.getCode());
        String m1 = resourceDao.getImage(ResourceType.N95.getCode());
        String m2 = resourceDao.getImage(ResourceType.PM25.getCode());
        String m3 = resourceDao.getImage(ResourceType.Ori.getCode());
        int a1 = TypeUtil.getNumber(receiveStationDao.getAmount(stationId, ResourceType.N95.getCode()));
        int a2 = TypeUtil.getNumber(receiveStationDao.getAmount(stationId, ResourceType.PM25.getCode()));
        int a3 = TypeUtil.getNumber(receiveStationDao.getAmount(stationId, ResourceType.Ori.getCode()));
        StationResourceVO v1 = new StationResourceVO(ResourceType.N95.getMsg(),a1,a1*w1,m1);
        StationResourceVO v2 = new StationResourceVO(ResourceType.PM25.getMsg(),a2,a2*w2,m2);
        StationResourceVO v3 = new StationResourceVO(ResourceType.Ori.getMsg(),a3,a3*w3,m3);
        StationResourceVO[] vos = new StationResourceVO[]{v1,v2,v3};
        log.info(">>>接收站物资清点-列表 获取成功");
        return ResultVOUtil.success(vos);
    }

    @ResponseBody
    @GetMapping("/list")
    @ApiOperation(value = "获取接收站")
    public ResultVO getDesignStation(@RequestParam(value = "province", required = true)String province) {
        List<ReceiveStation> list;
        if (province == null|| StringUtil.isEmpty(province)) {
            list = receiveStationDao.findAll(StationStatus.CHECKED.getCode());
        }
        else {
            list = receiveStationDao.findByProvince(province, StationStatus.CHECKED.getCode());
        }
        log.info(">>>获取接收站 成功");
        return ResultVOUtil.success(list);
    }

    @ResponseBody
    @GetMapping("/resource")
    @ApiOperation(value = "获取接收站物资情况")
    public ResultVO getDesignStationResource(@RequestParam(value = "rid")Integer rid) {
        int n1 = TypeUtil.getNumber(receiveStationDao.getAmount(rid, ResourceType.N95.getCode()));
        int n2 = TypeUtil.getNumber(receiveStationDao.getAmount(rid, ResourceType.PM25.getCode()));
        int n3 = TypeUtil.getNumber(receiveStationDao.getAmount(rid, ResourceType.Ori.getCode()));
        String s1 = genStr(n1, ResourceType.N95.getMsg());
        String s2 = genStr(n2, ResourceType.PM25.getMsg());
        String s3 = genStr(n3, ResourceType.Ori.getMsg());
        log.info(">>>获取接收站物资情况 成功");
        return ResultVOUtil.success(s1+s2+s3);
    }

    private String genStr(int num, String type) {
        if (num >= 0) return "拥有"+type+num+"  个；";
        else return "缺少"+type+(-num)+"  个；";
    }

}

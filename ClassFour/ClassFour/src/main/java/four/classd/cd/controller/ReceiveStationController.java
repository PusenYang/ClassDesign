package four.classd.cd.controller;

import com.github.pagehelper.util.StringUtil;
import four.classd.cd.config.SwaggerConfig;
import four.classd.cd.dao.ReceiveStationDao;
import four.classd.cd.model.entity.DesignStation;
import four.classd.cd.model.entity.ReceiveStation;
import four.classd.cd.model.enums.ResourceType;
import four.classd.cd.model.enums.StationStatus;
import four.classd.cd.model.vo.ResultVO;
import four.classd.cd.util.ResultVOUtil;
import four.classd.cd.util.TypeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/r_station")
@Slf4j
@CrossOrigin(origins = "*")
@Api(tags = SwaggerConfig.RECEIVE_TAG)
public class ReceiveStationController {

    @Autowired
    private ReceiveStationDao receiveStationDao;

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

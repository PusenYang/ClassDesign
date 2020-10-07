package four.classd.cd.controller;

import com.github.pagehelper.util.StringUtil;
import four.classd.cd.config.SwaggerConfig;
import four.classd.cd.dao.DesignStationDao;
import four.classd.cd.model.entity.DesignStation;
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

import java.util.ArrayList;
import java.util.List;

@RestController
@Api(tags = SwaggerConfig.DESIGN_TAG)
@Slf4j
@RequestMapping("/d_station")
@CrossOrigin(origins = "*")
public class DesignStationController {

    @Autowired
    private DesignStationDao designStationDao;

    @ResponseBody
    @GetMapping("/list")
    @ApiOperation(value = "获取调配站")
    public ResultVO getDesignStation(@RequestParam(value = "province", required = true)String province) {
        List<DesignStation> list;
        if (province == null|| StringUtil.isEmpty(province)) {
            list = designStationDao.findAll(StationStatus.CHECKED.getCode());
        }
        else {
            list = designStationDao.findByProvince(province, StationStatus.CHECKED.getCode());
        }
        log.info(">>>获取调配站 成功");
        return ResultVOUtil.success(list);
    }

    @ResponseBody
    @GetMapping("/resource")
    @ApiOperation(value = "获取调配站物资情况")
    public ResultVO getDesignStationResource(@RequestParam(value = "did")Integer did) {
        StringBuilder sb = new StringBuilder();
        sb.append("拥有N95口罩 ")
           .append(TypeUtil.getNumber(designStationDao.getAmount(did, ResourceType.N95.getCode()))).append(" 个；")
           .append("拥有PM2.5口罩 ")
           .append(TypeUtil.getNumber(designStationDao.getAmount(did, ResourceType.PM25.getCode()))).append(" 个；")
           .append("拥有普通医用外科口罩")
           .append(TypeUtil.getNumber(designStationDao.getAmount(did, ResourceType.Ori.getCode()))).append(" 个；");
        log.info(">>>获取调配站物资情况 成功");
        return ResultVOUtil.success(sb.toString());
    }

}

package four.classd.cd.controller;

import four.classd.cd.config.SwaggerConfig;
import four.classd.cd.dao.PositionDao;
import four.classd.cd.model.entity.Position;
import four.classd.cd.model.vo.ResultVO;
import four.classd.cd.util.ResultVOUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/position")
@Slf4j
@Api(tags = SwaggerConfig.INFO_TAG)
public class PositionController {

    @Autowired
    private PositionDao positionDao;

    @ResponseBody
    @GetMapping("/all")
    @ApiOperation(value = "测试接口")
    public ResultVO getAll() {
        List<Position> positionList = positionDao.findAll();
        log.info(">>>位置 获取所有");
        return ResultVOUtil.success(positionList);
    }

}

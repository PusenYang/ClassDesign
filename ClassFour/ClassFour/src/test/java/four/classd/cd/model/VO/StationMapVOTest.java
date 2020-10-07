package four.classd.cd.model.VO;

import four.classd.cd.model.vo.StationMapVO;
import four.classd.cd.util.ResultVOUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author PusenYang
 * @version 1.0
 * @date 2020/10/8 1:43
 */
public class StationMapVOTest {

    @Test
    public void stationMapVOTest() {
        StationMapVO vo = new StationMapVO();
        vo.setLat(56.33);
        vo.setLng(78.99);
        vo.setName("432342");
        vo.setType("1");
        vo.setAddress("543534");
        vo.setManager("43543");
        StationMapVO vo1 = new StationMapVO();
        vo1.setLat(56.33);
        vo1.setLng(78.99);
        vo1.setName("432342");
        vo1.setType("1");
        vo1.setAddress("543534");
        vo1.setManager("43543");
        List<StationMapVO> res = new ArrayList<>();
        res.add(vo);
        res.add(vo1);
        System.out.println(ResultVOUtil.success(res));
    }
}

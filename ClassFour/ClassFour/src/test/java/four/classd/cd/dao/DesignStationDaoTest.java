package four.classd.cd.dao;

import four.classd.cd.model.entity.DesignStation;
import four.classd.cd.model.entity.Position;
import four.classd.cd.model.enums.StationStatus;
import four.classd.cd.util.DateUtil;
import four.classd.cd.util.KeyUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DesignStationDaoTest {

    @Autowired
    private DesignStationDao designStationDao;

    @Test
    public void testGetDeisng() {
        List<String> stations = designStationDao.findNameByProvince("湖南省",StationStatus.CHECKED.getCode());
        List<DesignStation> designStations = designStationDao.findByStatus(1);
        System.out.println("-----------------------");
        for (String s : stations) {
            System.out.println(s);
        }
    }

    @Test
    public void testGetDesign() {
        List<String> stations = designStationDao.findNameByProvinceO("湖南省");
        System.out.println("-----------------------");
        for (String s : stations) {
            System.out.println(s);
        }
    }

    @Test
    public void testGetByStatus() {
        List<DesignStation> list = designStationDao.findByStatus(StationStatus.CHECKED.getCode());
    }

    @Test
    public void testAddStation() {
        DesignStation ds = new DesignStation();
        ds.setId(KeyUtil.generateUserID());
        ds.setName("test-name");
        ds.setAddress("test_address");
//        ds.setLongitude();
//        ds.setLatitude();
        ds.setCounty("天心区");
        ds.setProvince("湖南省");
        ds.setCity("长沙市");
//        ds.setImage();
//        ds.setRemark();
        ds.setManagerId(3);
        ds.setManagerName("我是人");
        ds.setManagerPhone("898");
        ds.setStatus(1);
        ds.setCreateTime(DateUtil.getTimeNow());
        System.out.println(designStationDao.addStation(ds));
    }

    /**
     * 验证开启驼峰命名法
     */
    @Test
    public void getAll() {
        List<DesignStation> list = designStationDao.findAlls();
        for (DesignStation ds : list) {
            System.out.println(ds.getAddress());
            System.out.println(ds.getManagerName());
            System.out.println(ds.getManagerId());
            System.out.println("------");
        }
    }
}

package four.classd.cd.dao;

import four.classd.cd.model.entity.Position;
import four.classd.cd.util.KeyUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PositionDaoTest {

    @Autowired
    private PositionDao positionDao;

    @Test
    public void testAddPosition() {
        Position p = new Position();
        p.setId(KeyUtil.generatePosID());
        p.setProvince("湖南省");
        p.setCity("长沙市");
        p.setCounty("天心区");
        System.out.println(positionDao.addPosition(p));
    }
}

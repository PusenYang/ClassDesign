package four.classd.cd.dao;

import four.classd.cd.model.entity.DesignOrder;
import four.classd.cd.model.entity.DesignStationManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author PusenYang
 * @version 1.0
 * @date 2020/10/11 18:21
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class DesignOrderDaoTest {
    @Autowired
    private DesignOrderDao designOrderDao;

    @Autowired
    private DesignStationManagerDao designStationManagerDao;

    @Test
    public void testGetOrder() {
        DesignStationManager user = designStationManagerDao.findByToken("i1e8wN20gj9R");

        int userId = user.getId();
        System.out.println("负责人id: "+userId);
        List<DesignOrder> row = new ArrayList<>();

        row = designOrderDao.findByDesign(userId);
        for (DesignOrder dd : row) {
            System.out.println(dd.getDesignManagerId());
        }

    }
}

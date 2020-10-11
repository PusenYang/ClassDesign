package four.classd.cd.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.PortUnreachableException;

/**
 * @author PusenYang
 * @version 1.0
 * @date 2020/10/11 18:00
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ReceiveStationManagerDaoTest {

    @Autowired
    private ReceiveStationManagerDao receiveStationManagerDao;
    @Test
    public void testGetManager() {
        System.out.println(receiveStationManagerDao.findByStation(13149774).getUsername());
    }
}

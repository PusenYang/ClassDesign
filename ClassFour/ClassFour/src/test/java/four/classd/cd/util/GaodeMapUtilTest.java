package four.classd.cd.util;

import org.junit.Test;

import java.io.IOException;

/**
 * @author PusenYang
 * @version 1.0
 * @date 2020/10/8 0:01
 */
public class GaodeMapUtilTest {

    @Test
    public void testGetLocation() throws IOException {
        String address = "浙江省杭州市余杭区西溪北苑北区94栋";
        System.out.println(GaodeMapUtil.getLocation(address));
    }
}

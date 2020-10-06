package four.classd.cd.util;


import org.junit.jupiter.api.Test;

public class MD5UtilTest {
    @Test
    public void testMD5() {
        System.out.println(MD5Util.MD5("MySalt" + "admin"));
    }
}

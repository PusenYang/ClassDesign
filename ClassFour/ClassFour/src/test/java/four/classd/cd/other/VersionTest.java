package four.classd.cd.other;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringBootVersion;
import org.springframework.core.SpringVersion;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class VersionTest {
    @Test
    public void testGetVersion() {
        System.out.println(SpringVersion.getVersion());
        System.out.println(SpringBootVersion.getVersion());
    }

    @Test
    public void testGetPath() {
        System.out.println(System.getProperty("user.dir"));
    }
}

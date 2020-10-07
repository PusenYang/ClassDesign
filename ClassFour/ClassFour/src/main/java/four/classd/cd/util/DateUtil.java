package four.classd.cd.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static String getTimeNow() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        return df.format(new Date());
    }
}

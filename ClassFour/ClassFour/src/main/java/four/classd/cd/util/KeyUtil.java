package four.classd.cd.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @author PusenYang
 * @version 1.0
 * @date 2020/9/25 0:42
 */
public class KeyUtil {

    /**
     * 生成8位ID
     * @return
     */
    public static Integer generateOrdID() {
        return Integer.parseInt(generateStr(8));
    }

    public static Integer generateUserID() {return Integer.parseInt(generateStr(6));}

    public static Integer generatePosID() {return Integer.parseInt(generateStr(4));}

    /**
     * 生成订单号: mmdd+随机8位
     * @return
     */
    public static String generateNumber() {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("MMdd");
        Date date = new Date();
        return sdf.format(date) + generateStr(8);
    }

    /**
     * 生成加密盐值
     * @return
     */
    public static String generateSalt() {
        return generateStr2(6);
    }

    /**
     * 生成token
     * @return
     */
    public static String generateToken() {
        return generateStr2(8);
    }

    /**
     * 随机产生一个length长度字符串
     * @param length 长度
     * @return 随机字符串
     */
    private static String generateStr(int length) {
        String base = "123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 随机产生一个length长度字符串
     * @param length 长度
     * @return 随机字符串
     */
    private static String generateStr2(int length) {
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
}

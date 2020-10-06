package four.classd.cd.util;

import java.security.MessageDigest;

/**
 * @author PusenYang
 * @version 1.0
 * @date 2020/9/25 0:41
 */
public class MD5Util {

    public static String MD5(String key) {
        char hexDigits[] = { // 十六进制
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        try {
            // 1.构建输入内容的字节数组
            byte[] byteInput = key.getBytes();
            // 2.获得MD5摘要算法的 MessageDigest 对象
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            // 3.更新字节数组摘要
            messageDigest.update(byteInput);
            // 4.获得密文字节数组
            byte[] cipherText = messageDigest.digest();
            // 5.把密文转换成十六进制的字符串形式
            int len = cipherText.length;
            char str[] = new char[len * 2]; // 开辟一个二倍长的字符数组
            int k = 0;
            for (int i = 0; i < len; i++) { // 遍历密文字节数组
                byte byte0 = cipherText[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 第一位执行无符号右移，并执行按位与操作
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }
}

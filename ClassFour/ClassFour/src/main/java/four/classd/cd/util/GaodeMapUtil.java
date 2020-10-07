package four.classd.cd.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import four.classd.cd.constant.GaodeMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * @author PusenYang
 * @version 1.0
 * @date 2020/10/6 20:59
 */
public class GaodeMapUtil {

    /**
     * 高德地图通过地址获取经纬度
     * 经度在前 维度在后
     */
    public static String[] getLocation(String address) throws IOException {
        String geturl = "http://restapi.amap.com/v3/geocode/geo?key="+GaodeMap.KEY +"&address="+address;
        String location = "";

        URL url = new URL(geturl);    // 把字符串转换为URL请求地址
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();// 打开连接
        connection.connect();// 连接会话
        // 获取输入流
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {// 循环读取流
            sb.append(line);
        }
        br.close();// 关闭流
        connection.disconnect();// 断开连接
        JSONObject a = JSON.parseObject(sb.toString());
        JSONArray sddressArr = JSON.parseArray(a.get("geocodes").toString());
        JSONObject c = JSON.parseObject(sddressArr.get(0).toString());
        location = c.get("location").toString();
        String[] arr = location.split(",");
        return arr;
    }

    /*
     * 根据经纬度计算两点之间的距离（单位KM）
     */
    public static double getDistance(String add1,String add2) {
        String[] temp1 = add1.split(",");
        String[] temp2 = add2.split(",");
        if (temp1.length < 2 || temp2.length < 2)
            return -1;
        double latitude1 = Double.valueOf(temp1[1]);
        double latitude2 = Double.valueOf(temp2[1]);
        double longitude1 = Double.valueOf(temp1[0]);
        double longitude2 = Double.valueOf(temp2[0]);
        double Lat1 = rad(latitude1); // 纬度
        double Lat2 = rad(latitude2);
        double a = Lat1 - Lat2;// 两点纬度之差
        double b = rad(longitude1) - rad(longitude2); // 经度之差
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(Lat1) * Math.cos(Lat2) * Math.pow(Math.sin(b / 2), 2)));// 计算两点距离的公式
        s = s * 6378137.0;// 弧长乘地球半径（半径为米）
        s = Math.round(s * 10000d) / 10000d;// 精确距离的数值
        // 四舍五入 保留一位小数
        //DecimalFormat df = new DecimalFormat("#.0");
        return s/1000;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.00; // 角度转换成弧度
    }
}

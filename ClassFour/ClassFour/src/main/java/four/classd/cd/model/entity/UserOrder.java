package four.classd.cd.model.entity;

import lombok.Data;

import java.util.List;

/**
 * @author PusenYang
 * @version 1.0
 * @date 2020/9/19 15:17
 *
 * 物资单: 从普通用户到配送站, 统一整理后到接收站
 */
@Data
public class UserOrder {

    private int id;
    private String number; // 订单编号
    private int status; // 订单状态

    /* 人员相关 */
    private int userId; // 报备人员ID
    private String username;
    private String phone; // 资源提供人的电话

    /* 资源相关 */
    private int totalWeight; // 资源总重量
    private int totalAmount; // 资源总数量
    private List<UserOrderResource> resList; //资源列表

    private String address; // 资源提供的地点
    private double longitude; // 经度
    private double latitude; // 纬度

    private String theDate; // 资源提供的日期
    private String designDate; // 调配站收到的日期
    private String receiveDate; // 接收站收到的日期

    private int designId; // 提供到哪个配送站
    private String designName; // 配送站的名字
    private String designAddress; // 配送站的地点

    private String createTime;
}

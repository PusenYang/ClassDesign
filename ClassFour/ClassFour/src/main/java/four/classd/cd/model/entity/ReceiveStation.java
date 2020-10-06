package four.classd.cd.model.entity;

import four.classd.cd.model.enums.StationStatus;
import lombok.Data;

/**
 * @author PusenYang
 * @version 1.0
 * @date 2020/9/19 15:01
 *
 * 各地的接收站, 一般设立在市
 */
@Data
public class ReceiveStation {
    private int id;
    private String name;
    private String address; // 具体地点
    private double longitude; // 经度
    private double latitude; // 纬度
    private String province; // 省份
    private String city; // 城市

    private String image; // 资格审查图片链接
    private String remark; // 备注

    private int managerId; // 负责人ID
    private String managerName; // 负责人姓名
    private String managerPhone; // 负责人联系方式

    private int status; // 审核状态

    private String createTime;

    private Double distance; // 计算距离用得到
}

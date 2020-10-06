package four.classd.cd.model.entity;

import four.classd.cd.model.enums.StationStatus;
import lombok.Data;

/**
 * @author PusenYang
 * @version 1.0
 * @date 2020/9/19 15:02
 *
 * 调配站, 一般精确到区县
 */
@Data
public class DesignStation {

    private int id;
    private String name;
    private String address; // 具体地点
    private double longitude; // 经度
    private double latitude; // 纬度
    private String county; // 县
    private String province; // 省份
    private String city; // 城市

    private String image; // 资格审查图片链接
    private String remark; // 备注

    private int managerId; // 负责人ID
    private String managerName; // 负责人姓名
    private String managerPhone; // 负责人联系方式

    private int status; // 审核状态

    private String createTime;
}

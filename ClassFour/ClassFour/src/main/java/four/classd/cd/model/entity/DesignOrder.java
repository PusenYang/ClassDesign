package four.classd.cd.model.entity;

import lombok.Data;

import java.util.List;

/**
 * @author PusenYang
 * @version 1.0
 * @date 2020/9/24 0:54
 *
 * 配送单: 从配送站到接收站
 */
@Data
public class DesignOrder {
    private int id;
    private String number; // 订单编号
    private int status; // 订单状态

    /* 人员相关 */
    private int designManagerId; // 调配站负责人员ID
    private String designManagerName; // 调配站负责人员姓名
    private String designManagerPhone; // 调配站负责人员电话

    private String goManagerName; // 运输负责人姓名
    private String goManagerPhone; // 运输负责人电话

    private int receiveManagerId;
    private String receiveManagerName;
    private String receiveManagerPhone; // 接收站负责人电话

    /* 资源相关 */
    private int totalWeight; // 资源总重量
    private int totalAmount; // 资源总数量
    private List<DesignOrderResource> resList; //资源列表

    /* 调配站 */
    private int startId; // 调配站ID
    private String startName; // 调配站名字
    private String startAddress; // 调配站地点
    private String startDate; // 开始运送日期

    /* 接收站 */
    private int endId; // 接收站ID
    private String endName; // 接收站名字
    private String endAddress; // 接收站地点
    private String endDate; // 到达日期(确认接收时更新)

    private String createTime;
}

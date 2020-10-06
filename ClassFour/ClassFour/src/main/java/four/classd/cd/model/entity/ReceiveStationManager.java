package four.classd.cd.model.entity;

import lombok.Data;

/**
 * @author PusenYang
 * @version 1.0
 * @date 2020/9/19 15:27
 *
 * 各地接收站的管理人员
 */
@Data
public class ReceiveStationManager {
    private int id;
    private String username;
    private String password;
    private String salt;
    private String token;
    private String phone;
    private String avatar;
    private String idCard; // 身份证
    private String createTime;

    /* 站点相关 */
    private int stationId; // 所管辖的调配站的ID
}

package four.classd.cd.model.entity;

import lombok.Data;

/**
 * @author PusenYang
 * @version 1.0
 * @date 2020/9/23 23:33
 *
 * 调配站管理人员,负责维护给地调配站的信息,接收普通用户的资源报备信息
 */
@Data
public class DesignStationManager {
    private int id;
    private String username;
    private String password;
    private String salt;
    private String avatar;
    private String token;
    private String phone;
    private String idCard; // 身份证
    private String createTime;

    /* 站点相关 */
    private int stationId; // 所管辖的调配站的ID
}

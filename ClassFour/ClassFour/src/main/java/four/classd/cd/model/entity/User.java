package four.classd.cd.model.entity;

import lombok.Data;

/**
 * @author PusenYang
 * @version 1.0
 * @date 2020/9/19 15:06
 *
 * 普通人员, 负责报备个人的物资情况
 */
@Data
public class User {

    private int id;
    private String username;
    private String avatar; // 头像链接
    private String password;
    private String salt;
    private String token; // 验证
    private String phone;
    private String address;
    private String createTime;
}

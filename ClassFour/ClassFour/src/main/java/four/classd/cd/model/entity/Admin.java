package four.classd.cd.model.entity;

import lombok.Data;

/**
 * @author PusenYang
 * @version 1.0
 * @date 2020/9/19 15:03
 *
 * 超级管理员, 负责对配送站和资源站的资格进行审查
 */
@Data
public class Admin {
    private int id;
    private String username;
    private String password;
    private String avatar;
    private String salt;
    private String token;
}
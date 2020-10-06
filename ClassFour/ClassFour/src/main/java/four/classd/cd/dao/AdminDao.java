package four.classd.cd.dao;

import four.classd.cd.model.entity.Admin;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author PusenYang
 * @version 1.0
 * @date 2020/9/25 0:24
 */
@Mapper
@Component
public interface AdminDao {

    static String ADMIN_TABLE = "class_design.admin";
    static String INSERT_FIELD = "id, username, password, avatar, salt";

    // 添加管理员
    @Insert({"insert into",ADMIN_TABLE,"(",INSERT_FIELD,") values(#{id},#{username},#{password},#{avatar},#{salt})"})
    Integer addAdmin(Admin admin);

    // 更新用户信息
    @Update({"update",ADMIN_TABLE,"set token = #{token} where username = #{username}"})
    Integer updateToken(@Param("username")String username, @Param("token")String token);

    // 查询用户: ID/用户名/手机号
    @Select({"select * from",ADMIN_TABLE,"where id=#{key} or username=#{key}"})
    Admin findAdmin(@Param("key") String key);

    @Select({"select * from",ADMIN_TABLE,"where token = #{token}"})
    Admin findByToken(@Param("token")String token);

    // 查询用户
    @Select({"select * from",ADMIN_TABLE,"where username=#{username}"})
    Admin findByName(@Param("username") String username);

    // 查询所有用户
    @Select({"select * from",ADMIN_TABLE,})
    List<Admin> findAll();

}

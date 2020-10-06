package four.classd.cd.dao;

import four.classd.cd.model.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author PusenYang
 * @version 1.0
 * @date 2020/9/25 0:14
 */
@Mapper
@Component
public interface UserDao {

    static String USER_TABLE = "class_design.user";
    static String INSERT_FIELD = "id, username, avatar, password, salt, phone, address, create_time";

    // 添加用户
    @Insert({"insert into",USER_TABLE,"(",INSERT_FIELD,") values(#{id},#{username},#{avatar},#{password},#{salt},#{phone},#{address}, ,#{createTime})"})
    Integer addUser(User user);

    @Update({"update",USER_TABLE,"(",INSERT_FIELD,") values(#{id},#{username},#{avatar},#{password},#{salt},#{phone},#{address})"})
    Integer updateToken(@Param("username")String username, @Param("token")String token);

    @Select({"select * from",USER_TABLE,"where token = #{token}"})
    User findByToken(@Param("token")String token);

    @Select({"select * from",USER_TABLE,"where username = #{username}"})
    User findByName(@Param("username")String username);

    @Select({"select id from",USER_TABLE,"where token = #{token}"})
    Integer findIdByToken(@Param("token")String token);

    // 查询用户: ID/用户名/手机号
    @Select({"select * from",USER_TABLE,"where username = #{key} or id = #{key} or phone = #{key}"})
    User findUser(@Param("key") String key);

    // 查询所有用户
    @Select({"select * from",USER_TABLE,})
    List<User> findAll();

}

package four.classd.cd.dao;

import four.classd.cd.model.entity.DesignStationManager;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author PusenYang
 * @version 1.0
 * @date 2020/9/25 0:27
 */
@Mapper
@Component
public interface DesignStationManagerDao {
    static String DSM_TABLE = "class_design.design_station_manager";
    static String INSERT_FIELD = "id, username, password, salt, phone, id_card, station_id, avatar,create_time";

    // 添加负责人
    @Insert({"insert into",DSM_TABLE,"(",INSERT_FIELD,") values(#{id},#{username},#{password},#{salt},#{phone},#{idCard},#{stationId},#{avatar},#{createTime})"})
    Integer addManager(DesignStationManager manager);

    @Update({"update",DSM_TABLE,"set token = #{token} where username = #{key} or phone = #{key}"})
    Integer updateToken(@Param("key")String key, @Param("token")String token);

    // 查询负责人
    @Select({"select * from",DSM_TABLE,"where username = #{key} or id = #{key} or phone = #{key}"})
    DesignStationManager findManager(@Param("key") String key);

    @Select({"select * from",DSM_TABLE,"where token = #{token}"})
    DesignStationManager findByToken(@Param("token")String token);

    @Select({"select * from",DSM_TABLE,"where username = #{username}"})
    DesignStationManager findByName(String username);

    // 查询站点的所有负责人
    @Select({"select * from",DSM_TABLE,"where station_id = #{stationId}"})
    List<DesignStationManager> findByStation(@Param("stationId")int stationId);

    // 查询所有
    @Select({"select * from",DSM_TABLE,})
    List<DesignStationManager> findAll();
}

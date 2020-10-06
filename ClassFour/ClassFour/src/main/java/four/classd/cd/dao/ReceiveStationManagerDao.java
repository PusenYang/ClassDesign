package four.classd.cd.dao;

import four.classd.cd.model.entity.ReceiveStationManager;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author PusenYang
 * @version 1.0
 * @date 2020/9/25 0:30
 */
@Mapper
@Component
public interface ReceiveStationManagerDao {

    static String RSM_TABLE = "class_design.receive_station_manager ";
    static String INSERT_FIELD = "id, username, password, salt, phone, id_card, avatar, station_id, create_time";

    // 添加负责人
    @Insert({"insert into",RSM_TABLE,"(",INSERT_FIELD,") values(#{id},#{username},#{password},#{salt},#{phone},#{idCard},#{avatar},#{stationId},#{createTime})"})
    Integer addManager(ReceiveStationManager manager);

    @Update({"update",RSM_TABLE,"set token = #{token} where username = #{username}"})
    Integer updateToken(@Param("username")String username, @Param("token")String token);

    // 查询负责人
    @Select({"select * from",RSM_TABLE,"where username = #{key} or id = #{key} or phone = #{key}"})
    ReceiveStationManager findManager(@Param("key") String key);

    @Select({"select * from",RSM_TABLE,"where token = #{token}"})
    ReceiveStationManager findByToken(@Param("token")String token);

    // 查询站点的负责人
    @Select({"select * from",RSM_TABLE,"where station_id = #{stationId)"})
    ReceiveStationManager findByStation(@Param("stationId")int stationId);

    // 查询所有
    @Select({"select * from",RSM_TABLE,})
    List<ReceiveStationManager> findAll();
}

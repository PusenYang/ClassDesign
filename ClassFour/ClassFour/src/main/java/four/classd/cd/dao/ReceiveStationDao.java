package four.classd.cd.dao;

import four.classd.cd.model.entity.ReceiveStation;
import four.classd.cd.model.entity.ReceiveStationResource;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * @author PusenYang
 * @version 1.0
 * @date 2020/10/2 17:38
 */
@Mapper
@Component
public interface ReceiveStationDao {

    static String RS_TABLE = "class_design.receive_station";
    static String INSERT_FIELD = "id, name, address, longitude, latitude, province, city, image, remark, manager_id, manager_name, manager_phone, status, create_time";

    static String RSS_TABLE = "class_design.receive_station_resource";
    static String INSERT_FIELD2 = "station_id, type_code, amount";

    // 增加资源
    @Insert({"insert into",RSS_TABLE,"(",INSERT_FIELD2,") values(#{stationId},#{typeCode},#{amount})"})
    Integer addResource(ReceiveStationResource resource);

    // 修改资源数量
    @Update({"update",RSS_TABLE,"set amount=#{amount} where station_id=#{id} and type_code=#{code}"})
    Integer updateResource(@Param("id")int id, @Param("code")int code, @Param("amount")int amount);

    @Select({"select amount from",RSS_TABLE,"where station_id=#{id} and type_code=#{code}"})
    Integer getAmount(@Param("id")int id, @Param("code")int code);

    @Select({"select sum(amount) from",RSS_TABLE,"where station_id=#{id}"})
    Integer getTotalAmount(@Param("id")int id);

    // 添加站点
    @Insert({"insert into",RS_TABLE,"(",INSERT_FIELD,") values(#{id},#{name},#{address},#{longitude},#{latitude},#{province},#{city},#{image},#{remark},#{managerId},#{managerName},#{managerPhone},#{status},#{createTime})"})
    Integer addStation(ReceiveStation station);

    @Update({"update",RS_TABLE,"set status = #{status} where id = #{id}"})
    Integer updateStatus(@Param("status")int status, @Param("id")int id);

    // 根据状态查询站点
    @Select({"select * from",RS_TABLE,"where status = #{status}"})
    List<ReceiveStation> findByStatus(@Param("status") int status);

    // 查询站点
    @Select({"select * from",RS_TABLE,"where id = #{id}"})
    ReceiveStation findById(@Param("id")int id);

    // 查询站点
    @Select({"select * from",RS_TABLE,"where name = #{name}"})
    ReceiveStation findByName(@Param("name")String name);

    // 查询站点
    @Select({"select name from",RS_TABLE,"where id = #{id}"})
    String findNameById(@Param("id")int id);

    // 根据省份查询站点
    @Select({"select * from",RS_TABLE,"where status = #{status} and province = #{province}"})
    List<ReceiveStation> findByProvince(@Param("province")String province, @Param("status")int status);

    @Select({"select name from",RS_TABLE,"where status = #{status} and province = #{province}"})
    List<String> findNameByProvince(@Param("province")String province, @Param("status")int status);

    // 根据市查询站点

    // 查询所有站点
    @Select({"select name from",RS_TABLE,"where status = #{status}"})
    List<String> findAll(@Param("status")int status);

    @Select({"select * from",RS_TABLE,"where status = #{status}"})
    List<ReceiveStation> findWAll(@Param("status")int status);

    // 查询所有站点
    @Select({"select * from",RS_TABLE,})
    List<ReceiveStation> findAlls();

}

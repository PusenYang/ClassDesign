package four.classd.cd.dao;

import four.classd.cd.model.entity.DesignStation;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author PusenYang
 * @version 1.0
 * @date 2020/9/25 0:32
 */
@Mapper
@Component
public interface DesignStationDao {

    static String DS_TABLE = "class_design.design_station";
    static String INSERT_FIELD = "id, name, address, longitude, latitude, province, city, county, image, remark, manager_id, manager_name, manager_phone, status,create_time";

    static String DSS_TABLE = "class_design.design_station_resource";
    static String INSERT_FIELD2 = "station_id, type_code, amount";

    // 修改资源数量
    @Update({"update",DSS_TABLE,"set amount=#{amount} where station_id=#{id} and type_code=#{code}"})
    Integer updateResource(@Param("id")int id, @Param("code")int code, @Param("amount")int amount);

    @Select({"select amount from",DSS_TABLE,"where station_id=#{id} and type_code=#{code}"})
    Integer getAmount(@Param("id")int id, @Param("code")int code);

    @Select({"select sum(amount) from",DSS_TABLE,"where station_id=#{id}"})
    Integer getTotalAmount(@Param("id")int id);

    // 添加站点
    @Insert({"insert into",DS_TABLE,"(",INSERT_FIELD,") values (#{id},#{name},#{address},#{longitude},#{latitude},#{province},#{city},#{county},#{image},#{remark},#{managerId},#{managerName},#{managerPhone},#{status},#{createTime})"})
    Integer addStation(DesignStation station);

    // 更新站点
    @Update({"update",DS_TABLE,"set status = #{status} where id = #{id}"})
    Integer updateStatus(@Param("status")int status, @Param("id")int id);

    // 根据状态查询站点
    @Select({"select * from",DS_TABLE,"where status = #{status}"})
    List<DesignStation> findByStatus(@Param("status") int status);

    // 查询站点
    @Select({"select * from",DS_TABLE,"where id = #{id}"})
    DesignStation findById(@Param("id")int id);

    // 根据省份查询站点
    @Select({"select * from",DS_TABLE,"where province = #{province}"})
    DesignStation findByProvince(@Param("province")String province);

    // 根据市查询站点
    @Select({"select * from",DS_TABLE,"where city = #{city}"})
    DesignStation findByCity(@Param("city")String city);

    @Select({"select * from",DS_TABLE,"where county = #{county}"})
    DesignStation findByCounty(@Param("county")String county);

    // 查询所有站点
    @Select({"select * from",DS_TABLE,})
    List<DesignStation> findAll();

}

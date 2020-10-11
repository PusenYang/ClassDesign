package four.classd.cd.dao;

import four.classd.cd.model.entity.DesignStation;
import four.classd.cd.model.entity.DesignStationResource;
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
    public static String DS_TABLE = "class_design.design_station";
    static String INSERT_FIELD = "id, name, address, longitude, latitude, province, city, county, image, remark, manager_id, manager_name, manager_phone, status,create_time";

    static String DSS_TABLE = "class_design.design_station_resource";
    static String INSERT_FIELD2 = "station_id, type_code, amount";

    // 增加资源
    @Insert({"insert into",DSS_TABLE,"(",INSERT_FIELD2,") values(#{stationId},#{typeCode},#{amount})"})
    Integer addResource(DesignStationResource resource);

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

    @Select({"select name from",DS_TABLE,"where province = #{province}"})
    List<String> findNameByProvinceO(@Param("province") String province);

    // 查询站点
    @Select({"select * from",DS_TABLE,"where id = #{id}"})
    DesignStation findById(@Param("id")int id);

    @Select({"select city from",DS_TABLE,"where name = #{name}"})
    String findCityByName(@Param("name")String name);

    @Select({"select city from",DS_TABLE,"where id = #{id}"})
    String findCityById(@Param("id")int id);

    @Select({"select * from",DS_TABLE,"where name = #{name}"})
    DesignStation findByName(@Param("name")String name);

    // 根据省份查询站点
    @Select({"select * from",DS_TABLE,"where province = #{province} and status=#{status}"})
    List<DesignStation> findByProvince(@Param("province")String province, @Param("status")int status);

    @Select({"select name from",DS_TABLE,"where status=#{status} and province=#{province}"})
    List<String> findNameByProvince(@Param("province")String province, @Param("status")int status);

    // 根据市查询站点
    @Select({"select * from",DS_TABLE,"where city = #{city}"})
    List<DesignStation> findByCity(@Param("city")String city);

    @Select({"select * from",DS_TABLE,"where county = #{county}"})
    List<DesignStation> findByCounty(@Param("county")String county);

    // 查询所有站点
    @Select({"select * from",DS_TABLE,})
    List<DesignStation> findAlls();

    @Select({"select * from",DS_TABLE,"where id = #{id} and name=#{name}"})
    DesignStation findByIdAndName(@Param("id")int id,@Param("name")String name);

    @Select({"select name from",DS_TABLE,"where status = #{status}"})
    List<String> findAll(@Param("status")int status);

    @Select({"select * from",DS_TABLE,"where status = #{status}"})
    List<DesignStation> findWAll(@Param("status")int status);
}

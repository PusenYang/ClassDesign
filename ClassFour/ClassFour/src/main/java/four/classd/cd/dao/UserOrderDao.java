package four.classd.cd.dao;

import four.classd.cd.model.entity.UserOrder;
import four.classd.cd.model.entity.UserOrderResource;
import org.apache.ibatis.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static four.classd.cd.dao.DesignStationDao.DS_TABLE;

/**
 * @author PusenYang
 * @version 1.0
 * @date 2020/10/2 22:06
 */
@Mapper
@Component
public interface UserOrderDao {
    static String USER_ORDER_TABLE = "class_design.user_order";
    static String INSERT_FIELD = "id, number, status, user_id, username, phone, total_weight, total_amount, address, longitude, latitude, the_date, design_id, design_name, design_address,create_time，design_date,receive_date";

    static String USER_ORDER_RESOURCE_TABLE = "class_design.user_order_resource";
    static String INSERT_FIELD2 = "number, type_code, amount";

    @Insert({"insert into",USER_ORDER_TABLE,"(",INSERT_FIELD,") values(#{id},#{number},#{status},#{userId},#{username},#{phone},#{totalWeight},#{totalAmount},#{address},#{longitude},#{latitude},#{theDate},#{designId},#{designName},#{designAddress},#{createTime},#{designDate},#{receiveDate})"})
    Integer addOrder(UserOrder userOrder);

    @Select({"select * from",USER_ORDER_TABLE,"where id = #{id}"})
    UserOrder findById(@Param("id")int id);

    @Select({"select * from",USER_ORDER_TABLE,"where number = #{number}"})
    UserOrder findByNumber(@Param("number")String number);

    @Select({"select * from",USER_ORDER_TABLE,"where user_id = #{userId}"})
    List<UserOrder> findByUser(@Param("userId")int userId);

    @Select({"select * from",USER_ORDER_TABLE,"as u inner join,",DS_TABLE,"as d on u.design_id=d.id where u.user_id=#{userId} and d.province=#{province}"})
    List<UserOrder> findByUserProvince(@Param("userId")int userId, @Param("province")String province);

    @Select({"select * from",USER_ORDER_TABLE,"as u inner join,",DS_TABLE,"as d on u.design_id=d.id where u.user_id=#{userId} and d.province=#{province} and u.status=#{status}"})
    List<UserOrder> findByUserProvinceStatus(@Param("userId")int userId, @Param("province")String province, @Param("status")int status);

    @Select({"select * from",USER_ORDER_TABLE,"where user_id=#{userId} and status=#{status}"})
    List<UserOrder> findByUserStatus(@Param("userId")int userId, @Param("status")int status);

    @Select({"select * from",USER_ORDER_TABLE,"where design_id = #{designId}"})
    List<UserOrder> findByDesign(@Param("designId")int designId);

    @Select({"select * from",USER_ORDER_TABLE,"where design_id = #{designId} and status = #{status}"})
    List<UserOrder> findByDesignStatus(@Param("designId")int designId, @Param("status")int status);

    @Update({"update",USER_ORDER_TABLE,"set status = #{status} where number = #{number}"})
    Integer updateStatusByNumber(@Param("status")int status, @Param("number")String number);

    // 更新日期
    @Update({"update",USER_ORDER_TABLE,"set the_date=#{date} where number=#{number}"})
    Integer updateTheDateByNumber(@Param("number")String number,@Param("date")String date);

    // 更新日期
    @Update({"update",USER_ORDER_TABLE,"set design_date=#{date} where number=#{number}"})
    Integer updateDesignDateByNumber(@Param("number")String number,@Param("date")String date);

    // 更新日期
    @Update({"update",USER_ORDER_TABLE,"set receive_date=#{date} where number=#{number}"})
    Integer updateReceiveDateByNumber(@Param("number")String number,@Param("date")String date);

    @Select({"select the_date from",USER_ORDER_TABLE,"where number=#{number}"})
    String getTheDate(@Param("number")String number);

    @Select({"select design_date from",USER_ORDER_TABLE,"where number=#{number}"})
    String getDesignDate(@Param("number")String number);

    @Select({"select receive_date from",USER_ORDER_TABLE,"where number=#{number}"})
    String getReceiveDate(@Param("number")String number);

    // 添加某种资源
    @Insert({"insert into", USER_ORDER_RESOURCE_TABLE,"(",INSERT_FIELD2,") values(#{number},#{typeCode},#{amount})"})
    Integer addResource(UserOrderResource orderDetail);

    // 获取某种资源
    @Select({"select amount from",USER_ORDER_RESOURCE_TABLE,"where number=#{number} and type_code=#{code}"})
    Integer getResource(@Param("number")String number,@Param("code")int code);
}

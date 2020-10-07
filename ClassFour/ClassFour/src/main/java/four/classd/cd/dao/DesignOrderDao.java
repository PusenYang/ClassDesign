package four.classd.cd.dao;

import four.classd.cd.model.entity.DesignOrder;
import four.classd.cd.model.entity.DesignOrderResource;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author PusenYang
 * @version 1.0
 * @date 2020/10/2 22:07
 */
@Component
@Mapper
public interface DesignOrderDao {
    static String DESIGN_ORDER_TABLE = "class_design.design_order";
    static String INSERT_FIELD = "id, number, status, design_manager_id, design_manager_name, design_manager_phone, go_manager_name, go_manager_phone, receive_manager_id, receive_manager_name, receive_manager_phone, total_weight, total_amount, start_id, start_name, start_address, start_date, end_id, end_name, end_address, end_date,create_time";

    static String DESIGN_ORDER_RESOURCE_TABLE = "class_design.design_order_resource";
    static String INSERT_FIELD2 = "number, type_code, amount";

    @Insert({"insert into",DESIGN_ORDER_TABLE,"(",INSERT_FIELD,") values(#{id},#{number},#{status},#{designManagerId},#{designManagerName},#{designManagerPhone},#{goManagerName},#{goManagerPhone},#{receiveManagerId},#{receiveManagerName},#{receiveManagerPhone},#{totalWeight},#{totalAmount},#{startId},#{startName},#{startAddress},#{startDate},#{endId},#{endName},#{endAddress},#{endDate},#{createTime})"})
    Integer addOrder(DesignOrder designOrder);

    @Select({"select * from",DESIGN_ORDER_TABLE,"where id = #{id}"})
    DesignOrder findById(@Param("id")int id);

    @Select({"select * from",DESIGN_ORDER_TABLE,"where number = #{number}"})
    DesignOrder findByNumber(@Param("number")String number);

    @Select({"select * from",DESIGN_ORDER_TABLE,"where start_id = #{designId}"})
    List<DesignOrder> findByDesign(@Param("designId")int designId);

    @Select({"select * from",DESIGN_ORDER_TABLE,"where end_id = #{receiveId}"})
    List<DesignOrder> findByReceive(@Param("receiveId")int receiveId);

    @Update({"update",DESIGN_ORDER_TABLE,"set status = #{status} where number = #{number}"})
    Integer updateStatusByNumber(@Param("status")int status, @Param("number")String number);

    @Insert({"insert into",DESIGN_ORDER_RESOURCE_TABLE,"(",INSERT_FIELD2,") values(#{number},#{typeCode},#{amount})"})
    Integer addResource(DesignOrderResource orderDetail);

    // 获取某种资源
    @Select({"select amount from",DESIGN_ORDER_RESOURCE_TABLE,"where number=#{number} and type_code=#{code}"})
    Integer getResource(@Param("number")String number,@Param("code")int code);
}

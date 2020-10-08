package four.classd.cd.dao;

import four.classd.cd.model.entity.Resource;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author PusenYang
 * @version 1.0
 * @date 2020/10/3 9:47
 */
@Mapper
@Component
public interface ResourceDao {
    static String RESOURCE_TABLE = "class_design.resource";
    static String INSERT_FIELD = "id, type_code, type_name, weight, image";

    @Insert({"insert into",RESOURCE_TABLE,"(",INSERT_FIELD,") values(#{id},#{typeCode},#{typeName},#{weight},#{image})"})
    Integer addResource(Resource resource);

    @Select({"select weight from",RESOURCE_TABLE,"where type_code = #{typeId}"})
    Integer getWeight(@Param("typeId")int typeId);

    @Select({"select image from",RESOURCE_TABLE,"where type_code=#{typeId}"})
    String getImage(@Param("typeId")int typeId);
}

package four.classd.cd.dao;

import four.classd.cd.model.entity.GoManager;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface GoManagerDao {
    static String GO_MANAGER_TABLE = "class_design.go_manager";

    @Select({"select * from",GO_MANAGER_TABLE,"where city = #{city}"})
    List<GoManager> findByCity(@Param("city") String city);

    @Select({"select * from",GO_MANAGER_TABLE,"where id = #{id}"})
    GoManager findById(@Param("id")int id);
}

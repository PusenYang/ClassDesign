package four.classd.cd.dao;

import four.classd.cd.model.entity.GoManager;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface GoManagerDao {
    static String GO_MANAGER_TABLE = "class_design.go_manager";

    @Select({"select * from",GO_MANAGER_TABLE,"where city = #{city}"})
    GoManager findByCity(String city);
}

package four.classd.cd.dao;

import four.classd.cd.model.entity.Position;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface PositionDao {
    static String POSITION_TABLE = "class_design.position";
    static String INSERT_FIELD = "id, province, city, county";

    @Insert({"insert into",POSITION_TABLE,"(",INSERT_FIELD,") values(#{id},#{province},#{city},#{county})"})
    Integer addPosition(Position position);

    @Select({"select * from",POSITION_TABLE,})
    List<Position> findAll();

    @Select({"select province from",POSITION_TABLE,})
    List<String> findAllProvince();
}

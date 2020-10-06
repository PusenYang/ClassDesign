package four.classd.cd.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @author PusenYang
 * @version 1.0
 * @date 2020/9/25 0:52
 */
@Data
public class ListVO<T> {

    List<T> list;
    long total; // 总的数据条数, 用于分页
    int pageSize; // 每页显示的数据条数
}

package four.classd.cd.model.entity;

import lombok.Data;

/**
 * 位置：在数据库的最小分割单位是区县
 */
@Data
public class Position {
    private int id;
    private String province;
    private String city;
    private String county; // 区县
}

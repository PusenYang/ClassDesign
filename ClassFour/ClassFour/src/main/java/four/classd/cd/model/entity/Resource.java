package four.classd.cd.model.entity;

import lombok.Data;

/**
 * @author PusenYang
 * @version 1.0
 * @date 2020/9/19 15:15
 *
 * 资源
 */
@Data
public class Resource {
    private int id;
    private int typeCode; // 资源类型
    private String typeName; // 资源类型名字
    private long weight; // 单个资源的重量
    private String image; // 资源图片链接
}

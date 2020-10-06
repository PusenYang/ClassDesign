package four.classd.cd.model.entity;

import four.classd.cd.model.enums.ResourceType;
import lombok.Data;

/**
 * @author PusenYang
 * @version 1.0
 * @date 2020/10/3 11:33
 *
 * 订单的中间态: 记录订单的资源种类和数量
 */
@Data
public class UserOrderResource {
    private String number;
    private ResourceType type;
    private int amount;
}

package four.classd.cd.model.entity;

import four.classd.cd.model.enums.ResourceType;
import lombok.Data;

/**
 * @author PusenYang
 * @version 1.0
 * @date 2020/10/3 17:12
 */
@Data
public class DesignOrderResource {
    private String number;
    private int typeCode;
    private int amount;
}

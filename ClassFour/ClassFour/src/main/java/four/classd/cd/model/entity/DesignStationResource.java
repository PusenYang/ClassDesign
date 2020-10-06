package four.classd.cd.model.entity;

import four.classd.cd.model.enums.ResourceType;
import lombok.Data;

/**
 * @author PusenYang
 * @version 1.0
 * @date 2020/9/24 1:22
 *
 * 调配站物资信息
 */
@Data
public class DesignStationResource {
    private int stationId;
    private int typeCode;
    private int amount;
}

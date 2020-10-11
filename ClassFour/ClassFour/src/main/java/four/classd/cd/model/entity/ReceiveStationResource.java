package four.classd.cd.model.entity;

import four.classd.cd.model.enums.ResourceType;
import lombok.Data;

/**
 * @author PusenYang
 * @version 1.0
 * @date 2020/9/25 0:56
 */
@Data
public class ReceiveStationResource {
    private int stationId;
    private int typeCode;
    private int amount;

    public ReceiveStationResource(int stationId, int typeCode, int amount) {
        this.stationId = stationId;
        this.typeCode = typeCode;
        this.amount = amount;
    }
}

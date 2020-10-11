package four.classd.cd.model.vo;

import lombok.Data;

@Data
public class StationResourceVO {
    private String type;
    private int totalAmount;
    private int totalWeight;
    private String image;
    private int typeCode;

    public StationResourceVO(String type, int totalAmount, int totalWeight, String image, int typeCode) {
        this.type = type;
        this.totalAmount = totalAmount;
        this.totalWeight = totalWeight;
        this.image = image;
        this.typeCode = typeCode;
    }
}

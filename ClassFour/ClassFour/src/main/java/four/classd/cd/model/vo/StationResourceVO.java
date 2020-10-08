package four.classd.cd.model.vo;

import lombok.Data;

@Data
public class StationResourceVO {
    private String type;
    private int totalAmount;
    private int totalWeight;
    private String image;

    public StationResourceVO(String type,int totalAmount, int totalWeight, String image) {
        this.type = type;
        this.totalAmount = totalAmount;
        this.totalWeight = totalWeight;
        this.image = image;
    }
}

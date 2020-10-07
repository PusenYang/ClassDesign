package four.classd.cd.model.vo;

import lombok.Data;

/**
 * @author PusenYang
 * @version 1.0
 * @date 2020/10/8 1:22
 */
@Data
public class StationMapVO {
    private double lat;
    private double lng;
    private String name;
    private String type;
    private String address;
    private String manager;
}

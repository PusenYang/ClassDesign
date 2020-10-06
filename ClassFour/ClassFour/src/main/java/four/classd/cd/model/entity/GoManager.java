package four.classd.cd.model.entity;

import lombok.Data;

@Data
public class GoManager {
    private int id;
    private String name;
    private String phone;
    private String city; // 所负责的城市
}

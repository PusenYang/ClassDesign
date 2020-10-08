package four.classd.cd.model.enums;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author PusenYang
 * @version 1.0
 * @date 2020/9/24 0:57
 *
 * 资源类型
 */
@Getter
public enum ResourceType {

    N95(1, "N95口罩"),
    PM25(2, "pm2.5口罩"),
    Ori(3, "普通医用外科口罩")

    ;

    private Integer code;
    private String msg;

    ResourceType(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static int getCode(String tmp) {
        if (tmp.equals(N95.getMsg())) return N95.getCode();
        else if (tmp.equals(PM25.getMsg())) return PM25.getCode();
        else return Ori.getCode();
    }
}

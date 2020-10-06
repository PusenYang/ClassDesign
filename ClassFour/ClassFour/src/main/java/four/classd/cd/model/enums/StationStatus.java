package four.classd.cd.model.enums;

import lombok.Getter;

/**
 * @author PusenYang
 * @version 1.0
 * @date 2020/10/2 17:54
 */
@Getter
public enum StationStatus {

    UN_CHECK(0, "待审核"),
    CHECKED(1, "审核通过")

    ;

    private Integer code;
    private String msg;

    StationStatus(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

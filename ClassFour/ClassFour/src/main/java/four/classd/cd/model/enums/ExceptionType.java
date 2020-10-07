package four.classd.cd.model.enums;

import lombok.Getter;

/**
 * @author PusenYang
 * @version 1.0
 * @date 2020/9/25 0:12
 */
@Getter
public enum  ExceptionType {

    SUCCESS(0,"成功"),
    PARAM_ERROR(400,"参数错误"),
    INFO_NULL(402,"信息为空"),
    AUTHORITY_ERROR(403, "权限不足"),
    SERVER_ERROR(500,"服务器错误")

    ;

    private Integer code;
    private String msg;

    ExceptionType(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

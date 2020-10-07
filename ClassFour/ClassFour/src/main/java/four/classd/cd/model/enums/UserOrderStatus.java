package four.classd.cd.model.enums;

import lombok.Getter;

/**
 * @author PusenYang
 * @version 1.0
 * @date 2020/9/24 1:04
 *
 * 物资单 / 调配单 状态
 */
@Getter
public enum UserOrderStatus {
    NEW(1, "已创建待收取"),
    DE_UNRECEIVE(2, "运往配送站"),
    DE_RECEIVE(3, "配送站已收取"),
    GOING(4, "运往接收站"),
    FINAL_RECEIVE(5, "接收站已确认")

    ;

    private Integer code;
    private String msg;

    UserOrderStatus(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

package four.classd.cd.model.enums;

/**
 * @author PusenYang
 * @version 1.0
 * @date 2020/9/24 1:09
 *
 * 配送单状态
 */
public enum DesignOrderStatus {
    NEW(1, "已创建, 待调配"),
    GOING(3, "物资运送中"),
    FINAL_RECEIVE(4, "接收站已收取")

    ;

    private Integer code;
    private String msg;

    DesignOrderStatus(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

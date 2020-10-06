package four.classd.cd.model.vo;

import lombok.Data;

/**
 * @author PusenYang
 * @version 1.0
 * @date 2020/9/25 0:50
 */
@Data
public class ResultVO<T> {

    private Integer code;
    private String message;
    private boolean success;
    private T data;
}

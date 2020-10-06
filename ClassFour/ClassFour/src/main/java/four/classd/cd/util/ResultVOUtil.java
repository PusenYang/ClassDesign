package four.classd.cd.util;

import four.classd.cd.model.enums.ExceptionType;
import four.classd.cd.model.vo.ResultVO;

/**
 * @author PusenYang
 * @version 1.0
 * @date 2020/9/25 0:50
 */
public class ResultVOUtil {

    /**
     * 简单返回
     * @return
     */
    public static ResultVO success() {
        ResultVO resultVO = new ResultVO();
        resultVO.setSuccess(true);
        resultVO.setCode(ExceptionType.SUCCESS.getCode());
        resultVO.setMessage(ExceptionType.SUCCESS.getMsg());
        return resultVO;
    }

    /**
     * 简单返回...
     * @param object
     * @return
     */
    public static ResultVO success(Object object) {
        ResultVO resultVO = new ResultVO();
        resultVO.setData(object);
        resultVO.setSuccess(true);
        resultVO.setCode(ExceptionType.SUCCESS.getCode());
        resultVO.setMessage(ExceptionType.SUCCESS.getMsg());
        return resultVO;
    }

    public static ResultVO success(Integer code, String message) {
        ResultVO resultVO = new ResultVO();
        resultVO.setSuccess(true);
        resultVO.setCode(code);
        resultVO.setMessage(message);
        return resultVO;
    }

    /**
     * 指定成功返回的提示
     * @param code
     * @param message
     * @param object
     * @return
     */
    public static ResultVO success(Integer code, String message, Object object) {
        ResultVO resultVO = new ResultVO();
        resultVO.setData(object);
        resultVO.setCode(code);
        resultVO.setSuccess(true);
        resultVO.setMessage(message);
        return resultVO;
    }

    /**
     * 错误往往没有data
     * @param code
     * @param message
     * @return
     */
    public static ResultVO error(Integer code, String message) {
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(code);
        resultVO.setSuccess(false);
        resultVO.setMessage(message);
        return resultVO;
    }
}

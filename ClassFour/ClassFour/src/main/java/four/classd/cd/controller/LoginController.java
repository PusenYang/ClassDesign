package four.classd.cd.controller;

import com.github.pagehelper.util.StringUtil;
import four.classd.cd.config.SwaggerConfig;
import four.classd.cd.constant.RoleConstant;
import four.classd.cd.dao.AdminDao;
import four.classd.cd.dao.DesignStationManagerDao;
import four.classd.cd.dao.ReceiveStationManagerDao;
import four.classd.cd.dao.UserDao;
import four.classd.cd.model.entity.Admin;
import four.classd.cd.model.entity.DesignStationManager;
import four.classd.cd.model.entity.ReceiveStationManager;
import four.classd.cd.model.entity.User;
import four.classd.cd.model.enums.ExceptionType;
import four.classd.cd.model.vo.ResultVO;
import four.classd.cd.util.KeyUtil;
import four.classd.cd.util.MD5Util;
import four.classd.cd.util.ResultVOUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author PusenYang
 * @version 1.0
 * @date 2020/10/1 20:31
 *
 * @CrossOrigin(origins = "*")用于解决ajax的跨域请求问题
 */
@RestController
@RequestMapping("/")
@Slf4j
@Api(tags = SwaggerConfig.LOGIN_TAG)
@CrossOrigin(origins = "*")
public class LoginController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private AdminDao adminDao;

    @Autowired
    private DesignStationManagerDao designStationManagerDao;

    @Autowired
    private ReceiveStationManagerDao receiveStationManagerDao;

    @ResponseBody
    @PostMapping("/login")
    @ApiOperation(value = "登录接口", notes = "通过flag字段区分不同的角色")
    public ResultVO login(@RequestBody(required = false)Map<String, Object> map) {
        /* 参数获取 */
        String username = map.get("username").toString();
        String password = map.get("password").toString();
        String flag = map.get("flag").toString();

        /* 判空 */
        if (StringUtil.isEmpty(username) || StringUtil.isEmpty(password)) {
            log.info(">>>登录 用户名或密码为空");
            return ResultVOUtil.error(ExceptionType.PARAM_ERROR.getCode(), "用户名或密码为空");
        }
        if (StringUtil.isEmpty(flag)) {
            log.info(">>>登录 还没有选择角色");
            return ResultVOUtil.error(ExceptionType.PARAM_ERROR.getCode(), "还没有选择角色");
        }

        /* 根据角色验证 */
        if (flag.equals(RoleConstant.ADMIN)) {
            return adminLogin(username, password);
        }
        else if (flag.equals(RoleConstant.DESIGN_MANAGER)) {
            return designLogin(username, password);
        }
        else if (flag.equals(RoleConstant.RECEIVE_MANAGER)) {
            return receiveLogin(username, password);
        }
        else {
            return userLogin(username, password);
        }
    }

    /**
     * 对应前端content-type是application/json
     * @param map
     * @return
     */
    @ResponseBody
    @PostMapping("/login_admin")
    @ApiOperation(value = "管理员登录接口", notes = "通过flag字段区分不同的角色")
    public ResultVO loginAdmin(@RequestBody(required = false)Map<String, Object> map, HttpServletResponse response) {
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = servletRequestAttributes.getRequest();
//        System.out.println(request.getContentType());
//        System.out.println(request.getRequestURL());
        /* 参数获取 */
        String username = map.get("username").toString();
        String password = map.get("password").toString();

        /* 判空 */
        if (StringUtil.isEmpty(username) || StringUtil.isEmpty(password)) {
            log.info(">>>登录 用户名或密码为空");
            return ResultVOUtil.error(ExceptionType.PARAM_ERROR.getCode(), "用户名或密码为空");
        }

        Admin user = adminDao.findAdmin(username);
        if (user == null) {
            log.info(">>>登录 该用户尚未注册 : "+ username);
            return ResultVOUtil.error(ExceptionType.PARAM_ERROR.getCode(), "该用户尚未注册");
        }
        if (! MD5Util.MD5(user.getSalt()+password).equals(user.getPassword())) {
            log.info(">>>登录 用户名或密码错误");
            return ResultVOUtil.error(ExceptionType.PARAM_ERROR.getCode(), "用户名或密码错误");
        }
        // 登录成功之后，更新token, 并返回
        String token = KeyUtil.generateToken();
        adminDao.updateToken(username, token);
        log.info(">>>管理员登录成功："+username);
        return ResultVOUtil.success(token);
    }

    /**
     * 管理员登录有一个独立的接口
     * @param username
     * @param password
     * @return
     */
    private ResultVO adminLogin(String username, String password) {
        Admin user = adminDao.findAdmin(username);
        if (user == null) {
            log.info(">>>登录 该用户尚未注册 : "+ username);
            return ResultVOUtil.error(ExceptionType.PARAM_ERROR.getCode(), "该用户尚未注册");
        }
        if (! MD5Util.MD5(user.getSalt()+password).equals(user.getPassword())) {
            log.info(">>>登录 用户名或密码错误");
            return ResultVOUtil.error(ExceptionType.PARAM_ERROR.getCode(), "用户名或密码错误");
        }
        // 登录成功之后，更新token, 并返回
        String token = KeyUtil.generateToken();
        adminDao.updateToken(username, token);
        return ResultVOUtil.success(token);
    }

    private ResultVO designLogin(String username, String password) {
        DesignStationManager user = designStationManagerDao.findManager(username);
        if (user == null) {
            log.info(">>>登录 该用户尚未注册 : "+ username);
            return ResultVOUtil.error(ExceptionType.PARAM_ERROR.getCode(), "该用户尚未注册");
        }
        if (! MD5Util.MD5(user.getSalt()+password).equals(user.getPassword())) {
            log.info(">>>登录 用户名或密码错误");
            return ResultVOUtil.error(ExceptionType.PARAM_ERROR.getCode(), "用户名或密码错误");
        }
        // 登录成功之后，更新token, 并返回
        String token = KeyUtil.generateToken();
        designStationManagerDao.updateToken(username, token);
        log.info(">>>调配站负责人登录成功："+username);
        return ResultVOUtil.success(token);
    }

    private ResultVO receiveLogin(String username, String password) {
        ReceiveStationManager user = receiveStationManagerDao.findManager(username);
        if (user == null) {
            log.info(">>>登录 该用户尚未注册 : "+ username);
            return ResultVOUtil.error(ExceptionType.PARAM_ERROR.getCode(), "该用户尚未注册");
        }
        if (! MD5Util.MD5(user.getSalt()+password).equals(user.getPassword())) {
            log.info(">>>登录 用户名或密码错误");
            return ResultVOUtil.error(ExceptionType.PARAM_ERROR.getCode(), "用户名或密码错误");
        }
        // 登录成功之后，更新token, 并返回
        String token = KeyUtil.generateToken();
        receiveStationManagerDao.updateToken(username, token);
        log.info(">>>接收站负责人登录成功："+username);
        return ResultVOUtil.success(token);
    }

    private ResultVO userLogin(String username, String password) {
        User user = userDao.findUser(username);
        if (user == null) {
            log.info(">>>登录 该用户尚未注册 : "+ username);
            return ResultVOUtil.error(ExceptionType.PARAM_ERROR.getCode(), "该用户尚未注册");
        }
        if (! MD5Util.MD5(user.getSalt()+password).equals(user.getPassword())) {
            log.info(">>>登录 用户名或密码错误");
            return ResultVOUtil.error(ExceptionType.PARAM_ERROR.getCode(), "用户名或密码错误");
        }
        // 登录成功之后，更新token, 并返回
        String token = KeyUtil.generateToken();
        userDao.updateToken(username, token);
        log.info(">>>个人用户登录成功："+username);
        return ResultVOUtil.success(token);
    }
}

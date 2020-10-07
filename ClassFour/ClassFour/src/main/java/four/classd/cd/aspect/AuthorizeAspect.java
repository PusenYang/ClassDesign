package four.classd.cd.aspect;

import four.classd.cd.annotation.Authorize;
import four.classd.cd.constant.RoleConstant;
import four.classd.cd.dao.AdminDao;
import four.classd.cd.dao.DesignStationManagerDao;
import four.classd.cd.dao.ReceiveStationManagerDao;
import four.classd.cd.dao.UserDao;
import four.classd.cd.model.entity.Admin;
import four.classd.cd.model.entity.DesignStationManager;
import four.classd.cd.model.entity.ReceiveStationManager;
import four.classd.cd.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Component
@Aspect
@Slf4j
public class AuthorizeAspect {

    @Autowired
    private AdminDao adminDao;

    @Autowired
    private DesignStationManagerDao designStationManagerDao;

    @Autowired
    private ReceiveStationManagerDao receiveStationManagerDao;

    @Autowired
    private UserDao userDao;

    @Pointcut("@annotation(four.classd.cd.annotation.Authorize)")
    private void permission() {
    }

    @Before("permission()")
    public void checkAuthority(JoinPoint joinPoint) throws Exception{
        MethodSignature joinPointObject = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = joinPointObject.getParameterNames();
        // 获取token参数的下标
        int idx = getIdx(parameterNames, "token");
        Object[] args = joinPoint.getArgs();
        String token = String.valueOf(args[idx]);
        if (token == null || token.isEmpty()) {
            log.info("未登录, 当前获取的token为空");
            throw new Exception("token为空，权限不足");
        }
        /* 根据role注解获取方法对应角色，从对应数据库中查找用户 */
        // 获取切入的 Method
        Signature signature = joinPoint.getSignature();
        Method method = joinPointObject.getMethod();

        // 如果方法没有权限注解，去搜索类上是否有注解
        boolean flag = method.isAnnotationPresent(Authorize.class);
        String role = null;
        if (flag) {
            Authorize annotation = method.getAnnotation(Authorize.class);
            role = annotation.role();
        } else {
            // 如果方法上没有注解，则搜索类上是否有注解
            Authorize classAnnotation = AnnotationUtils.findAnnotation(joinPointObject.getMethod().getDeclaringClass(), Authorize.class);
            role = classAnnotation.role();
        }

        if (! hasAuthorize(role, token)) {
            log.info("token没有对应用户：" + token);
            throw new Exception("错误的token，权限不足");
        }
    }

    private boolean hasAuthorize(String role, String token) {
        if (role.equals(RoleConstant.ADMIN)) {
            Admin admin = adminDao.findByToken(token);
            return admin != null;
        }
        else if (role.equals(RoleConstant.DESIGN_MANAGER)) {
            DesignStationManager manager = designStationManagerDao.findByToken(token);
            return manager != null;
        }
        else if (role.equals(RoleConstant.RECEIVE_MANAGER)) {
            ReceiveStationManager manager = receiveStationManagerDao.findByToken(token);
            return manager != null;
        }
        else if (role.equals(RoleConstant.USER)) {
            User user = userDao.findByToken(token);
            return user != null;
        }
        else {
            return false;
        }
    }

    private int getIdx(String[] array, String tar) {
        if (array.length < 1) return -1;
        else {
            for (int i = 0; i < array.length; i ++) {
                if (array[i].equals(tar)) return i;
            }
        }
        return -1;
    }
}

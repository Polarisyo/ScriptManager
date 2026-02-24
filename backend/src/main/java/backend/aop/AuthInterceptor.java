package backend.aop;


import backend.annotation.AuthCheck;
import backend.exception.BusinessException;
import backend.exception.ErrorCode;
import backend.exception.ThrowUtils;
import backend.model.enums.UserRoleEnum;
import backend.model.vo.UserVO;
import backend.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Objects;

@Aspect
@Component
public class AuthInterceptor {
    @Resource
    private UserService userService;

    /**
     * @param joinPoint
     * @param authCheck
     * @return
     */
    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        //获取当前用户
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        String mustRole = authCheck.mustRole();
        UserRoleEnum mustRoleEnum = UserRoleEnum.getEnumByValue(mustRole);
        //放行
        if (mustRoleEnum == null) {
            return joinPoint.proceed();
        }

        UserVO loginUser = userService.getLoginUser(request);
        UserRoleEnum userRoleEnum = UserRoleEnum.getEnumByValue(loginUser.getUserRole());
        if (userRoleEnum == null) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        //获取操作用户id
        Long reqUserId = getReqUserId(joinPoint);
        ThrowUtils.throwIf(!Objects.equals(reqUserId, loginUser.getId()), ErrorCode.NO_AUTH_ERROR);
        if (userRoleEnum != UserRoleEnum.ADMIN && userRoleEnum != mustRoleEnum) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        return joinPoint.proceed();
    }

    /**
     * 获取@RequestBody里的userId,通常为操作用户，项目的所有者id，用于校验当前用户和所有者是否一致
     * @param joinPoint
     */
    private Long getReqUserId(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Parameter[] parameters = method.getParameters();
        Object[] args = joinPoint.getArgs();
        Object arg = null;
        for(int i = 0; i < parameters.length; i++) {
            if(parameters[i].getAnnotation(org.springframework.web.bind.annotation.RequestBody.class) != null) {
                arg = args[i];
                break;
            }
        }
        ThrowUtils.throwIf(arg == null, ErrorCode.PARAMS_ERROR);
        return extractUserIdFromObject(arg, "userId");
    }


    /**
     * 从对象中提取userId字段值
     */
    private Long extractUserIdFromObject(Object obj, String fieldName) {
        try {
            // 首先尝试通过getter方法获取
            String getterName = "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
            try {
                Method getter = obj.getClass().getMethod(getterName);
                Object value = getter.invoke(obj);
                return value != null ? Long.parseLong(value.toString()) : null;
            } catch (NoSuchMethodException e) {
                // 如果没有getter方法，尝试直接访问字段
                Field field = obj.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                Object value = field.get(obj);
                return value != null ? Long.parseLong(value.toString()) : null;
            }
        } catch (Exception e) {
            // 如果字段不存在或类型不匹配，返回null
            return null;
        }
    }

}

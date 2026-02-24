package backend.annotation;

import backend.model.enums.UserRoleEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)

@Target(ElementType.METHOD)
public @interface AuthCheck {
    /**
     * 必须的权限
     *
     * @return
     */
    String def = UserRoleEnum.USER.getValue();
    String mustRole() default "user";
}

package backend.model.enums;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;

@Getter
public enum UserRoleEnum {
    USER("用户", "user"),
    ADMIN("管理员", "admin");

    private final String text;
    private final String value;


    UserRoleEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public UserRoleEnum getEnumByValue(String value) {
        if(StrUtil.isBlank(value)) {
            return null;
        }
        for(UserRoleEnum e : UserRoleEnum.values()) {
            if(StrUtil.equals(e.value, value)) {
                return e;
            }
        }
        return null;
    }
}

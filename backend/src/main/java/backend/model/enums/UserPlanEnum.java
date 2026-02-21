package backend.model.enums;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;

@Getter
public enum UserPlanEnum {
    FREE("免费", "free"),
    BASIC("会员", "basic"),
    PRO("高级会员", "pro"),
    ENTERPRISE("企业版", "enterprise");

    private final String text;
    private final String value;


    UserPlanEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public UserPlanEnum getEnumByValue(String value) {
        if(StrUtil.isBlank(value)) {
            return null;
        }
        for(UserPlanEnum e : UserPlanEnum.values()) {
            if(StrUtil.equals(e.value, value)) {
                return e;
            }
        }
        return null;
    }
}

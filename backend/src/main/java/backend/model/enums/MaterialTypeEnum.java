package backend.model.enums;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;

@Getter
public enum MaterialTypeEnum {
    BACKGROUND("背景", "background"), CHARACTER("角色", "character"), PROP("道具", "prop"), AUDIO("音效", "audio");
    private final String text;
    private final String value;

    MaterialTypeEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public static MaterialTypeEnum getEnumByValue(String value) {
        if (StrUtil.isBlank(value)) {
            return null;
        }
        for (MaterialTypeEnum e : MaterialTypeEnum.values()) {
            if (StrUtil.equals(e.value, value)) {
                return e;
            }
        }
        return null;
    }
}

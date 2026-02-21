package backend.model.enums;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;

@Getter
public enum ProjectStatusEnum {
    DRAFT("草稿", "draft"), PROGRESS("进行中", "progress"), COMPLETED("已完成", "completed");
    private final String text;
    private final String value;

    ProjectStatusEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public static ProjectStatusEnum getEnumByValue(String value) {
        if (StrUtil.isBlank(value)) {
            return null;
        }
        for (ProjectStatusEnum e : ProjectStatusEnum.values()) {
            if (StrUtil.equals(e.value, value)) {
                return e;
            }
        }
        return null;
    }
}

package backend.model.dto.project;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ProjectQueryRequest implements Serializable {
    /**
     * 项目名称
     */

    private String projectName;

    /**
     * 项目类型（如：短视频、广告、宣传片等）
     */

    private String projectType;

    /**
     * 是否公开（0:私有, 1:公开）
     */
    private Boolean isPublic;

    /**
     * 项目状态: draft(草稿)/inProgress(进行中)/completed(已完成)/archived(已归档)
     */

    private String projectStatus;
    /**
     * 项目所有者
     */
    private Long userId;
}

package backend.model.dto.project;

import backend.model.vo.UserVO;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ProjectAddRequest implements Serializable {
    /**
     * 项目名称
     */

    private String projectName;

    /**
     * 项目描述
     */

    private String projectDescription;

    /**
     * 项目类型（如：短视频、广告、宣传片等）
     */

    private String projectType;

    /**
     * 封面图URL
     */

    private String coverUrl;

    /**
     * 分镜数量
     */

    private Integer shotCount;

    /**
     * 总时长（秒）
     */

    private Integer totalDuration;

    /**
     * 是否公开（0:私有, 1:公开）
     */
    private Boolean isPublic;

}

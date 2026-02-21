package backend.model.vo;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * 项目表 实体类。
 *

 */
@Data
public class ProjectVO implements Serializable {


    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 项目ID，主键
     */

    private Long id;

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
     * 项目状态: draft(草稿)/inProgress(进行中)/completed(已完成)/archived(已归档)
     */

    private String projectStatus;

    /**
     * 创建用户ID
     */

    private Long userId;

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
     * 完成进度百分比（0-100）
     */

    private BigDecimal progressRate;

    /**
     * 是否公开（0:私有, 1:公开）
     */
    private Boolean isPublic;
    /**
     * 创建用户
     */
    private UserVO user;
}

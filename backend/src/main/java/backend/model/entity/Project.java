package backend.model.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.io.Serial;


/**
 * 项目表 实体类。
 *
 * @author <a href="https://github.com/Polarisyo">Polaris</a>
 */
@Data
@Table("project")
public class Project implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 项目ID，主键
     */
    @Id(keyType=KeyType.Generator, value= KeyGenerators.flexId)
    private Long id;

    /**
     * 项目名称
     */
    @Column("projectName")
    private String projectName;

    /**
     * 项目描述
     */
    @Column("projectDescription")
    private String projectDescription;

    /**
     * 项目类型（如：短视频、广告、宣传片等）
     */
    @Column("projectType")
    private String projectType;

    /**
     * 项目状态: draft(草稿)/inProgress(进行中)/completed(已完成)/archived(已归档)
     */
    @Column("projectStatus")
    private String projectStatus;

    /**
     * 创建用户ID
     */
    @Column("userId")
    private Long userId;

    /**
     * 封面图URL
     */
    @Column("coverUrl")
    private String coverUrl;

    /**
     * 分镜数量
     */
    @Column("shotCount")
    private Integer shotCount;

    /**
     * 总时长（秒）
     */
    @Column("totalDuration")
    private Integer totalDuration;

    /**
     * 完成进度百分比（0-100）
     */
    @Column("progressRate")
    private BigDecimal progressRate;

    /**
     * 最后操作的分镜序号
     */
    @Column("lastShotIndex")
    private Integer lastShotIndex;

    /**
     * 分镜顺序数组
     */
    @Column("shotOrder")
    private String shotOrder;

    /**
     * 当前版本号
     */
    @Column("currentVersion")
    private Integer currentVersion;

    /**
     * 最大版本号
     */
    @Column("maxVersion")
    private Integer maxVersion;

    /**
     * 是否为模板（0:否, 1:是）
     */
    @Column("isTemplate")
    private Boolean isTemplate;

    /**
     * 软删除标记（0:正常, 1:已删除）
     */
    @Column("isDeleted")
    private Boolean isDeleted;

    /**
     * 是否公开（0:私有, 1:公开）
     */
    @Column("isPublic")
    private Boolean isPublic;

    /**
     * 创建时间
     */
    @Column("createTime")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column("updateTime")
    private LocalDateTime updateTime;

    /**
     * 删除时间
     */
    @Column("deleteTime")
    private LocalDateTime deleteTime;
}

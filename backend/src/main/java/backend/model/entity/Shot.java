package backend.model.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

import java.io.Serial;


/**
 * 分镜主表 实体类。
 *
 * @author <a href="https://github.com/Polarisyo">Polaris</a>
 */
@Data
@Table("shot")
public class Shot implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 分镜ID，主键
     */
    @Id(keyType=KeyType.Generator, value= KeyGenerators.flexId)
    @Column("shotId")
    private Long shotId;

    /**
     * 所属项目ID
     */
    @Column("projectId")
    private Long projectId;

    /**
     * 分镜标题
     */
    @Column("shotTitle")
    private String shotTitle;

    /**
     * 分镜描述
     */
    @Column("shotDesc")
    private String shotDesc;

    /**
     * 分镜时长（秒）
     */
    private Integer duration;

    /**
     * 排序序号（用于拖拽排序）
     */
    @Column("orderIndex")
    private Integer orderIndex;

    /**
     * 分镜状态：draft(草稿)/inProgress(进行中)/completed(已完成)
     */
    @Column("shotStatus")
    private String shotStatus;

    /**
     * 分镜对应剧本内容
     */
    @Column("scriptContent")
    private String scriptContent;

    /**
     * 软删除标记（0:正常, 1:已删除）
     */
    @Column("isDeleted")
    private Boolean isDeleted;

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

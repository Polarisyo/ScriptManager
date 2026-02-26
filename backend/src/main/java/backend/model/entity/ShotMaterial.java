package backend.model.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

import java.io.Serial;


/**
 * 分镜素材关联表 实体类。
 *
 * @author <a href="https://github.com/Polarisyo">Polaris</a>
 */
@Data
@Table("shot_material")
public class ShotMaterial implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 素材ID
     */
    @Id(keyType=KeyType.Generator, value= KeyGenerators.flexId)
    @Column("materialId")
    private Long materialId;

    /**
     * 分镜ID
     */
    @Column("shotId")
    private Long shotId;

    /**
     * 素材类型：image(图片)/video(视频)/audio(音频)
     */
    @Column("materialType")
    private String materialType;

    /**
     * 素材角色：background(背景)/character(角色)/prop(道具)/audio(音效)
     */
    @Column("materialRole")
    private String materialRole;

    /**
     * 素材描述
     */
    @Column("materialDesc")
    private String materialDesc;

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
}

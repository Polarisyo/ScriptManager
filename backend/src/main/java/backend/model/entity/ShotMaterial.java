package backend.model.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;

import java.io.Serializable;
import java.time.LocalDateTime;

import java.io.Serial;


/**
 * 分镜素材关联表 实体类。
 *
 * @author <a href="https://github.com/Polarisyo">Polaris</a>
 */
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

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public Long getShotId() {
        return shotId;
    }

    public void setShotId(Long shotId) {
        this.shotId = shotId;
    }

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public String getMaterialRole() {
        return materialRole;
    }

    public void setMaterialRole(String materialRole) {
        this.materialRole = materialRole;
    }

    public String getMaterialDesc() {
        return materialDesc;
    }

    public void setMaterialDesc(String materialDesc) {
        this.materialDesc = materialDesc;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

}

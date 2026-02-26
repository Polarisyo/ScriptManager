package backend.model.dto.shotMaterial;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;


/**
 * 分镜主表 实体类。
 *

 */
@Data
public class ShotMaterialEditRequest implements Serializable {


    private Long userId;
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 素材ID
     */
    private Long materialId;

    /**
     * 素材类型：image(图片)/video(视频)/audio(音频)
     */

    private String materialType;

    /**
     * 素材角色：background(背景)/character(角色)/prop(道具)/audio(音效)
     */

    private String materialRole;

    /**
     * 素材描述
     */

    private String materialDesc;
}

package backend.service;

import backend.model.dto.shotMaterial.ShotMaterialAddRequest;
import backend.model.dto.shotMaterial.ShotMaterialEditRequest;
import backend.model.dto.shotMaterial.ShotMaterialQueryRequest;
import backend.model.entity.ShotMaterial;
import backend.model.vo.ShotMaterialVO;
import com.mybatisflex.core.service.IService;
import backend.model.entity.ShotMaterial;

import java.util.List;

/**
 * 分镜素材关联表 服务层。
 *
 * @author <a href="https://github.com/Polarisyo">Polaris</a>
 */
public interface ShotMaterialService extends IService<ShotMaterial> {

    /**
     *
     * @param request
     * @return
     */
    long addShotMaterial(ShotMaterialAddRequest request);

    /**
     *
     * @param request
     * @return
     */
    ShotMaterialVO editShotMaterial(ShotMaterialEditRequest request);

    /**
     *
     * @param request
     * @return
     */
    List<ShotMaterialVO> queryShotMaterial(ShotMaterialQueryRequest request);

    /**
     *
     * @param shotMaterial
     * @return
     */
    ShotMaterialVO getShotMaterialVO(ShotMaterial shotMaterial);
}

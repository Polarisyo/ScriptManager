package backend.service.impl;

import backend.exception.ErrorCode;
import backend.exception.ThrowUtils;
import backend.mapper.ShotMaterialMapper;
import backend.model.dto.shotMaterial.ShotMaterialAddRequest;
import backend.model.dto.shotMaterial.ShotMaterialEditRequest;
import backend.model.dto.shotMaterial.ShotMaterialQueryRequest;
import backend.model.entity.Project;
import backend.model.entity.Shot;
import backend.model.entity.ShotMaterial;
import backend.model.enums.MaterialTypeEnum;
import backend.model.vo.ShotMaterialVO;
import backend.service.ProjectService;
import backend.service.ShotMaterialService;
import backend.service.ShotService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 分镜素材关联表 服务层实现。
 *
 * @author <a href="https://github.com/Polarisyo">Polaris</a>
 */
@Service
public class ShotMaterialServiceImpl extends ServiceImpl<ShotMaterialMapper, ShotMaterial> implements ShotMaterialService {

    @Resource
    ShotService shotService;
    @Resource
    ProjectService projectService;

    @Override
    public long addShotMaterial(ShotMaterialAddRequest request) {
        Long userId = request.getUserId();
        Long shotId = request.getShotId();
        String materialType = request.getMaterialType();
        String materialRole = request.getMaterialRole();
        String materialDesc = request.getMaterialDesc();

        materialValid(materialType, materialDesc, materialRole, shotId, userId, true);
        ShotMaterial material = new ShotMaterial();
        BeanUtil.copyProperties(request, material);
        boolean save = this.save(material);
        ThrowUtils.throwIfSqlError(!save);

        return material.getMaterialId();
    }

    //校验素材参数
    private void materialValid(String materialType, String materialDesc, String materialRole, Long shotId, Long userId, boolean isAdd) {
        if (isAdd) {
            ThrowUtils.throwIf(!StrUtil.isAllNotBlank(materialType, materialDesc, materialRole), ErrorCode.PARAMS_ERROR);
        }
        if (materialType != null) {
            MaterialTypeEnum typeEnum = MaterialTypeEnum.getEnumByValue(materialType);
            ThrowUtils.throwIf(typeEnum == null, ErrorCode.PARAMS_ERROR);
        }

        ThrowUtils.throwIf(shotId <= 0, ErrorCode.PARAMS_ERROR);
        Shot shot = shotService.getById(shotId);
        ThrowUtils.throwIf(shot == null, ErrorCode.PARAMS_ERROR, "分镜不存在");
        Project project = projectService.getById(shot.getProjectId());
        ThrowUtils.throwIf(project == null, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(!Objects.equals(project.getUserId(), userId), ErrorCode.NO_AUTH_ERROR);
    }

    @Override
    public ShotMaterialVO editShotMaterial(ShotMaterialEditRequest request) {
        Long userId = request.getUserId();
        Long materialId = request.getMaterialId();
        String materialType = request.getMaterialType();
        String materialRole = request.getMaterialRole();
        String materialDesc = request.getMaterialDesc();
        ShotMaterial material = this.getById(materialId);
        ThrowUtils.throwIf(material == null, ErrorCode.PARAMS_ERROR);
        materialValid(materialType, materialDesc, materialRole, material.getShotId(), userId, false);
        BeanUtil.copyProperties(request, material);
        boolean res = this.updateById(material);
        ThrowUtils.throwIfSqlError(!res);

        return getShotMaterialVO(material);
    }

    @Override
    public List<ShotMaterialVO> queryShotMaterial(ShotMaterialQueryRequest request) {
        Long userId = request.getUserId();
        Long materialId = request.getMaterialId();
        Long shotId = request.getShotId();
        String materialType = request.getMaterialType();
        String materialRole = request.getMaterialRole();
        String materialDesc = request.getMaterialDesc();
        materialValid(materialType, materialDesc, materialRole, shotId, userId, false);
        QueryWrapper wrapper = new QueryWrapper();
         wrapper.eq("materialType", materialType)
                .eq("materialRole", materialRole)
                .eq("materialId", materialId)
                .eq("shotId", shotId)
                .like("materialDesc", materialDesc);
        List<ShotMaterial> shotMaterials = this.getMapper().selectListByQuery(wrapper);
        return shotMaterials.stream().map(this::getShotMaterialVO).toList();
    }

    @Override
    public ShotMaterialVO getShotMaterialVO(ShotMaterial shotMaterial) {
        if (shotMaterial == null) {
            return null;
        }
        ShotMaterialVO vo = new ShotMaterialVO();
        BeanUtil.copyProperties(shotMaterial, vo);
        return vo;
    }
}

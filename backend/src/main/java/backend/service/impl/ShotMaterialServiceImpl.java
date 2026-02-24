package backend.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import backend.model.entity.ShotMaterial;
import backend.mapper.ShotMaterialMapper;
import backend.service.ShotMaterialService;
import org.springframework.stereotype.Service;

/**
 * 分镜素材关联表 服务层实现。
 *
 * @author <a href="https://github.com/Polarisyo">Polaris</a>
 */
@Service
public class ShotMaterialServiceImpl extends ServiceImpl<ShotMaterialMapper, ShotMaterial>  implements ShotMaterialService{

}

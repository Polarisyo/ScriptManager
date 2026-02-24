package backend.service;

import backend.common.DeleteRequest;
import backend.model.dto.shot.ShotAddRequest;
import backend.model.dto.shot.ShotEditRequest;
import backend.model.dto.shot.ShotQueryRequest;
import backend.model.entity.Shot;
import backend.model.vo.ShotVO;
import com.mybatisflex.core.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 分镜主表 服务层。
 *
 * @author <a href="https://github.com/Polarisyo">Polaris</a>
 */
public interface ShotService extends IService<Shot> {
    /**
     *
     * @param request
     * @return
     */
    long addShot(ShotAddRequest request);

    /**
     *
     * @param request
     * @return
     */
    ShotVO editShot(ShotEditRequest request);

    @Transactional
    void deleteShot(DeleteRequest request);

    /**
     *
     * @param request
     * @return
     */
    List<ShotVO> queryShot(ShotQueryRequest request);

    /**
     *
     * @param shot
     * @return
     */
    ShotVO getShotVO(Shot shot);

}

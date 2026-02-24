package backend.service.impl;

import backend.common.DeleteRequest;
import backend.exception.ErrorCode;
import backend.exception.ThrowUtils;
import backend.mapper.ShotMapper;
import backend.model.dto.shot.ShotAddRequest;
import backend.model.dto.shot.ShotEditRequest;
import backend.model.dto.shot.ShotQueryRequest;
import backend.model.entity.Project;
import backend.model.entity.Shot;
import backend.model.enums.ProjectStatusEnum;
import backend.model.vo.ShotVO;
import backend.service.ProjectService;
import backend.service.ShotService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 分镜主表 服务层实现。
 *
 * @author <a href="https://github.com/Polarisyo">Polaris</a>
 */
@Service
public class ShotServiceImpl extends ServiceImpl<ShotMapper, Shot> implements ShotService {

    @Resource
    ProjectService projectService;

    @Override
    @Transactional
    public long addShot(ShotAddRequest request) {
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
        // 校验基本参数
        Long projectId = request.getProjectId();
        String shotTitle = request.getShotTitle();
        String shotDesc = request.getShotDesc();
        Integer duration = request.getDuration();
        Long userId = request.getUserId();
        Project project = validShotInfo(projectId, shotTitle, shotDesc, duration, userId);

        Shot shot = new Shot();
        BeanUtil.copyProperties(request, shot);
        shot.setShotStatus(ProjectStatusEnum.DRAFT.getValue());
        Shot lastShot = this.getMapper().selectOneByQuery(query().eq("projectId", projectId).orderBy("orderIndex", false));
        shot.setOrderIndex(lastShot.getOrderIndex() + 1);
        boolean save = this.save(shot);
        ThrowUtils.throwIf(!save, ErrorCode.SYSTEM_ERROR);

        //同步修改project 分镜数，时长，分镜顺序
        project.setShotCount(project.getShotCount() + 1);
        project.setTotalDuration(project.getTotalDuration() + request.getDuration());
        project.setShotOrder(JSONUtil.toJsonStr(request.getShotOrder()));
        projectService.updateById(project);

        return shot.getShotId();
    }

    private Project validShotInfo(Long projectId, String shotTitle, String shotDesc, Integer duration, Long userId) {
        ThrowUtils.throwIf(projectId <= 0, ErrorCode.PARAMS_ERROR, "项目不存在");
        ThrowUtils.throwIf(StrUtil.isBlank(shotTitle), ErrorCode.PARAMS_ERROR, "分镜名称不能为空");
        ThrowUtils.throwIf(shotDesc.length() > 125, ErrorCode.PARAMS_ERROR, "分镜描述超长");
        ThrowUtils.throwIf(duration > 60, ErrorCode.PARAMS_ERROR, "分镜时长不能超过60秒");

        Project project = projectService.getById(projectId);
        ThrowUtils.throwIf(project == null, ErrorCode.PARAMS_ERROR, "项目不存在");
        ThrowUtils.throwIf(!Objects.equals(userId, project.getUserId()),
                ErrorCode.PARAMS_ERROR, "项目创建者才能修改分镜");

        return project;
    }

    @Override
    @Transactional
    public ShotVO editShot(ShotEditRequest request) {
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
        Long shotId = request.getShotId();
        Long projectId = request.getProjectId();
        String shotTitle = request.getShotTitle();
        String shotDesc = request.getShotDesc();
        Integer duration = request.getDuration();
        String shotStatus = request.getShotStatus();

        ThrowUtils.throwIf(shotId <= 0, ErrorCode.PARAMS_ERROR);
        //校验参数，和是否有权限操作该项目
        Project project = validShotInfo(projectId, shotTitle, shotDesc, duration, request.getUserId());
        ProjectStatusEnum statusEnum = ProjectStatusEnum.getEnumByValue(shotStatus);
        ThrowUtils.throwIf(statusEnum == null, ErrorCode.PARAMS_ERROR);

        //根据参数校验是否修改project
        Shot shot = this.getById(shotId);
        ThrowUtils.throwIf(shot == null, ErrorCode.PARAMS_ERROR);
        if(!Objects.equals(duration, shot.getDuration())) {
            project.setTotalDuration(project.getTotalDuration() - shot.getDuration() + duration);
        }
        if(request.getShotOrder() != null) {
            project.setShotOrder(JSONUtil.toJsonStr(request.getShotOrder()));
        }
        boolean res = projectService.updateById(project);
        ThrowUtils.throwIfSqlError(!res);

        BeanUtil.copyProperties(request, shot);
        res = this.updateById(shot);
        ThrowUtils.throwIfSqlError(!res);
        return getShotVO(shot);
    }
    @Transactional
    @Override
    public void deleteShot(DeleteRequest request) {
        Long shotId = request.getId();
        Long userId = request.getUserId();
        ThrowUtils.throwIf(shotId <= 0, ErrorCode.PARAMS_ERROR);
        Shot shot = this.getById(shotId);
        ThrowUtils.throwIf(shot == null, ErrorCode.PARAMS_ERROR,"分镜不存在");
        Project project = projectService.getOne(query()
                .eq("userId", userId).eq("id", shot.getProjectId()));
        ThrowUtils.throwIf(project == null, ErrorCode.PARAMS_ERROR, "项目不存在");

        project.setTotalDuration(project.getTotalDuration() - shot.getDuration());
        project.setShotCount(project.getShotCount() - 1);
        Integer[] order = JSONUtil.toList(project.getShotOrder(), Integer.class).toArray(new Integer[0]);
        Integer[] newOrder = new Integer[order.length - 1];
        for(int i = 0, j = 0; j < order.length - 1; i++, j++) {
            if(!Objects.equals(order[i], shot.getOrderIndex())) {
                newOrder[j] = order[i];
            } else {
                j--;
            }
        }
        project.setShotOrder(JSONUtil.toJsonStr(newOrder));
        boolean res = projectService.updateById(project);
        ThrowUtils.throwIfSqlError(!res);
        res = this.removeById(shotId);
        ThrowUtils.throwIfSqlError(!res);
    }

    @Override
    public List<ShotVO> queryShot(ShotQueryRequest request) {
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
        Long projectId = request.getProjectId();
        String shotTitle = request.getShotTitle();
        Integer orderIndex = request.getOrderIndex();
        String shotStatus = request.getShotStatus();

        validShotInfo(projectId, "disused", "", 0, request.getUserId());

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.like("shotTitle", shotTitle)
                .eq("orderIndex", orderIndex)
                .eq("projectId", projectId)
                .eq(shotStatus, shotStatus);
        List<Shot> shots = this.getMapper().selectListByQuery(wrapper);
        return shots.stream().map(this::getShotVO).toList();
    }

    @Override
    public ShotVO getShotVO(Shot shot) {
        if (shot == null) {
            return null;
        }
        ShotVO shotVO = new ShotVO();
        BeanUtil.copyProperties(shot, shotVO);
        return shotVO;
    }
}

package backend.service.impl;

import backend.exception.ErrorCode;
import backend.exception.ThrowUtils;
import backend.mapper.ProjectMapper;
import backend.model.dto.project.ProjectAddRequest;
import backend.model.dto.project.ProjectEditRequest;
import backend.model.dto.project.ProjectQueryRequest;
import backend.model.entity.Project;
import backend.model.vo.ProjectVO;
import backend.service.ProjectService;
import backend.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 项目表 服务层实现。
 *
 * @author <a href="https://github.com/Polarisyo">Polaris</a>
 */
@Service
@Slf4j
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {

    @Resource
    UserService userService;

    private static void projectInfoValid(String projectName, String projectDescription, String projectType) {
        ThrowUtils.throwIf(StrUtil.isAllNotBlank(projectName, projectDescription, projectType), ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(projectName.length() > 100, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(projectDescription.length() > 500, ErrorCode.PARAMS_ERROR);
    }

    @Override
    public long addProject(ProjectAddRequest request) {
        ThrowUtils.throwIf(ObjectUtil.isNull(request), ErrorCode.PARAMS_ERROR);

        String projectName = request.getProjectName();
        String projectDescription = request.getProjectDescription();
        String projectType = request.getProjectType();

        projectInfoValid(projectName, projectDescription, projectType);
        Project project = new Project();
        BeanUtil.copyProperties(request, project);
        project.setShotOrder(JSONUtil.toJsonStr(new Integer[]{}));

        boolean save = this.save(project);
        ThrowUtils.throwIf(!save, ErrorCode.SYSTEM_ERROR);

        return project.getId();
    }

    @Override
    public ProjectVO editProject(ProjectEditRequest request) {
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
        Long id = request.getId();
        ThrowUtils.throwIf(id < 0, ErrorCode.PARAMS_ERROR);
        String projectName = request.getProjectName();
        String projectDescription = request.getProjectDescription();
        String projectType = request.getProjectType();
        projectInfoValid(projectName, projectDescription, projectType);

        Project project = this.getById(id);
        ThrowUtils.throwIf(project == null, ErrorCode.PARAMS_ERROR, "项目不存在");
        ThrowUtils.throwIf(!Objects.equals(project.getUserId(), request.getUserId()), ErrorCode.NO_AUTH_ERROR);

        BeanUtil.copyProperties(request, project);
        boolean res = this.updateById(project);
        ThrowUtils.throwIf(!res, ErrorCode.SYSTEM_ERROR);

        return getProjectVO(project);
    }

    @Override
    public List<ProjectVO> queryProject(ProjectQueryRequest request) {
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
        String projectName = request.getProjectName();
        String projectType = request.getProjectType();
        Boolean isPublic = request.getIsPublic();
        String projectStatus = request.getProjectStatus();
        Long userId = request.getUserId();
        ThrowUtils.throwIf(userId == null, ErrorCode.PARAMS_ERROR);
        QueryWrapper wrapper = QueryWrapper.create();
        wrapper.select().eq("userId", userId)
                .like("projectName", projectName)
                .eq("projectType", projectType)
                .eq("isPublic",isPublic)
                .eq("projectStatus", projectStatus);
        List<Project> projects = this.getMapper().selectListByQuery(wrapper);

        return projects.stream().map(this::getProjectVO).toList();
    }


    @Override
    public ProjectVO getProjectVO(Project project) {
        if (project == null) {
            return null;
        }
        ProjectVO vo = new ProjectVO();
        BeanUtil.copyProperties(project, vo, "shotOrder");
        String shotOrderStr = project.getShotOrder();
        if (shotOrderStr == null || shotOrderStr.trim().isEmpty() || "[]".equals(shotOrderStr.trim())) {
            vo.setShotOrder(new Integer[0]);
        } else {
            vo.setShotOrder(JSONUtil.toList(shotOrderStr, Integer.class).toArray(new Integer[0]));
        }
        return vo;
    }


}

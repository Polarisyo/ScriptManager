package backend.service.impl;

import backend.exception.ErrorCode;
import backend.exception.ThrowUtils;
import backend.mapper.ProjectMapper;
import backend.model.dto.project.ProjectAddRequest;
import backend.model.dto.project.ProjectEditRequest;
import backend.model.dto.project.ProjectQueryRequest;
import backend.model.entity.Project;
import backend.model.vo.ProjectVO;
import backend.model.vo.UserVO;
import backend.service.ProjectService;
import backend.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 项目表 服务层实现。
 *
 * @author <a href="https://github.com/Polarisyo">Polaris</a>
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {

    @Resource
    UserService userService;

    private static void projectInfoValid(String projectName, String projectDescription, String projectType) {
        ThrowUtils.throwIf(StrUtil.isAllNotBlank(projectName, projectDescription, projectType), ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(projectName.length() > 100, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(projectDescription.length() > 500, ErrorCode.PARAMS_ERROR);
    }

    @Override
    public long addProject(ProjectAddRequest request, HttpServletRequest req) {
        ThrowUtils.throwIf(ObjectUtil.isNull(request), ErrorCode.PARAMS_ERROR);
        UserVO loginUser = userService.getLoginUser(req);

        String projectName = request.getProjectName();
        String projectDescription = request.getProjectDescription();
        String projectType = request.getProjectType();

        projectInfoValid(projectName, projectDescription, projectType);
        Project project = new Project();
        BeanUtil.copyProperties(request, project);
        project.setUserId(loginUser.getId());

        boolean save = this.save(project);
        ThrowUtils.throwIf(!save, ErrorCode.SYSTEM_ERROR);

        return project.getId();
    }

    @Override
    public ProjectVO editProject(ProjectEditRequest request, HttpServletRequest req) {
        UserVO loginUser = userService.getLoginUser(req);

        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
        Long id = request.getId();
        ThrowUtils.throwIf(id < 0, ErrorCode.PARAMS_ERROR);
        String projectName = request.getProjectName();
        String projectDescription = request.getProjectDescription();
        String projectType = request.getProjectType();
        projectInfoValid(projectName, projectDescription, projectType);

        Project project = new Project();
        project.setUserId(loginUser.getId());
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
        wrapper.select().from("PROJECT")
                .where("USER_ID = {0}", userId)  // 用户ID为必填条件
                .and("PROJECT_NAME LIKE {0}", "%" + projectName + "%", StringUtils.isNotBlank(projectName))  // 项目名称模糊查询
                .and("PROJECT_TYPE = {0}", projectType, StringUtils.isNotBlank(projectType))  // 项目类型精确匹配
                .and("PROJECT_STATUS = {0}", projectStatus, StringUtils.isNotBlank(projectStatus))  // 项目状态精确匹配
                .and("IS_PUBLIC = {0}", isPublic);// 是否公开精确匹配
        List<Project> projects = this.getMapper().selectListByQuery(wrapper);

        return projects.stream().map(this::getProjectVO).toList();
    }


    @Override
    public ProjectVO getProjectVO(Project project) {
        if (project == null) {
            return null;
        }
        ProjectVO vo = new ProjectVO();
        BeanUtil.copyProperties(project, vo);
        return vo;
    }


}

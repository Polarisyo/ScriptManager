package backend.service;

import backend.model.dto.project.ProjectAddRequest;
import backend.model.dto.project.ProjectEditRequest;
import backend.model.dto.project.ProjectQueryRequest;
import backend.model.vo.ProjectVO;
import com.mybatisflex.core.service.IService;
import backend.model.entity.Project;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * 项目表 服务层。
 *
 * @author <a href="https://github.com/Polarisyo">Polaris</a>
 */
public interface ProjectService extends IService<Project> {
    /**
     *
     * @param request
     * @return
     */
    long addProject(ProjectAddRequest request, HttpServletRequest req);

    /**
     *
     * @param request
     * @return
     */
    ProjectVO editProject(ProjectEditRequest request, HttpServletRequest req);

    /**
     *
     * @param request
     * @return
     */
    List<ProjectVO> queryProject(ProjectQueryRequest request);

    /**
     *
     * @param project
     * @return
     */
    ProjectVO getProjectVO(Project project);
}

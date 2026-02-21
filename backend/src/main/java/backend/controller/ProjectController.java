package backend.controller;

import backend.common.BaseResponse;
import backend.common.ResultUtils;
import backend.model.dto.project.ProjectAddRequest;
import backend.model.dto.project.ProjectEditRequest;
import backend.model.dto.project.ProjectQueryRequest;
import backend.model.vo.ProjectVO;
import com.mybatisflex.core.paginate.Page;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import backend.model.entity.Project;
import backend.service.ProjectService;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * 项目表 控制层。
 *
 * @author <a href="https://github.com/Polarisyo">Polaris</a>
 */
@RestController
@RequestMapping("/project")
public class ProjectController {

    @Resource
    private ProjectService projectService;

    /**
     * 保存项目表。
     *
     * @param addRequest 项目表
     * @return {@code true} 保存成功，{@code false} 保存失败
     */
    @PostMapping("save")
    public BaseResponse<Long> save(@RequestBody ProjectAddRequest addRequest, HttpServletRequest req) {
        long id = projectService.addProject(addRequest, req);
        return ResultUtils.success(id);
    }

    /**
     * 根据主键删除项目表。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Long id) {
        return projectService.removeById(id);
    }

    /**
     * 根据主键更新项目表。
     *
     * @param editRequest 项目表
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PostMapping("edit")
    public BaseResponse<ProjectVO> edit(@RequestBody ProjectEditRequest editRequest, HttpServletRequest req) {
        ProjectVO projectVO = projectService.editProject(editRequest, req);
        return ResultUtils.success(projectVO);
    }

    /**
     * 查询所有项目表。
     *
     * @return 所有数据
     */
    @PostMapping("list")
    public BaseResponse<List<ProjectVO>> list(@RequestBody ProjectQueryRequest request) {
        List<ProjectVO> vos = projectService.queryProject(request);
        return ResultUtils.success(vos);
    }
}

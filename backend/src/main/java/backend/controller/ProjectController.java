package backend.controller;

import backend.annotation.AuthCheck;
import backend.common.BaseResponse;
import backend.common.ResultUtils;
import backend.model.dto.project.ProjectAddRequest;
import backend.model.dto.project.ProjectEditRequest;
import backend.model.dto.project.ProjectQueryRequest;
import backend.model.vo.ProjectVO;
import backend.model.vo.UserVO;
import backend.service.UserService;
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

    @Resource
    private UserService userService;

    /**
     * 保存项目表。
     *
     * @param addRequest 项目表
     * @return {@code true} 保存成功，{@code false} 保存失败
     */
    @AuthCheck
    @PostMapping("/add")
    public BaseResponse<Long> save(@RequestBody ProjectAddRequest addRequest) {
        long id = projectService.addProject(addRequest);
        return ResultUtils.success(id);
    }
    /**
     * 根据主键更新项目表。
     *
     * @param editRequest 项目表
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @AuthCheck
    @PostMapping("/edit")
    public BaseResponse<ProjectVO> edit(@RequestBody ProjectEditRequest editRequest) {
        ProjectVO projectVO = projectService.editProject(editRequest);
        return ResultUtils.success(projectVO);
    }

    /**
     * 查询所有项目表。
     *
     * @return 所有数据
     */
    @AuthCheck
    @PostMapping("/list")
    public BaseResponse<List<ProjectVO>> list(@RequestBody ProjectQueryRequest request) {
        List<ProjectVO> vos = projectService.queryProject(request);
        return ResultUtils.success(vos);
    }
}

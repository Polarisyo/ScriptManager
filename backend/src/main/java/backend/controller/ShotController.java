package backend.controller;

import backend.annotation.AuthCheck;
import backend.common.BaseResponse;
import backend.common.DeleteRequest;
import backend.common.ResultUtils;
import backend.common.SingleQueryRequest;
import backend.exception.ErrorCode;
import backend.exception.ThrowUtils;
import backend.model.dto.shot.ShotAddRequest;
import backend.model.dto.shot.ShotEditRequest;
import backend.model.dto.shot.ShotQueryRequest;
import backend.model.entity.Shot;
import backend.model.vo.ShotVO;
import backend.model.vo.UserVO;
import backend.service.ShotService;
import backend.service.UserService;
import com.mybatisflex.core.paginate.Page;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分镜主表 控制层。
 *
 * @author <a href="https://github.com/Polarisyo">Polaris</a>
 */
@RestController
@RequestMapping("/shot")
public class ShotController {

    @Resource
    private ShotService shotService;

    /**
     * 保存分镜主表。
     *
     * @param request 分镜主表
     * @return {@code true} 保存成功，{@code false} 保存失败
     */
    @AuthCheck
    @PostMapping("/add")
    public BaseResponse<Long> add(@RequestBody ShotAddRequest request) {
        long count = shotService.addShot(request);
        return ResultUtils.success(count);
    }

    /**
     * 根据主键更新分镜主表。
     *
     * @param request 分镜主表
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @AuthCheck
    @PutMapping("/edit")
    public BaseResponse<ShotVO> edit(@RequestBody ShotEditRequest request) {
        ShotVO shotVO = shotService.editShot(request);
        return ResultUtils.success(shotVO);
    }

    /**
     * 查询所有分镜主表。
     *
     * @return 所有数据
     */
    @AuthCheck
    @PostMapping("/list")
    public BaseResponse<List<ShotVO>> list(@RequestBody ShotQueryRequest request) {
        List<ShotVO> shotVOS = shotService.queryShot(request);
        return ResultUtils.success(shotVOS);
    }

    /**
     * 根据主键获取分镜主表。
     *
     * @param request 分镜主表主键
     * @return 分镜主表详情
     */
    @AuthCheck
    @PostMapping("getInfo")
    public BaseResponse<ShotVO> getInfo(@RequestBody SingleQueryRequest request) {
        Shot shot = shotService.getById(request.getId());
        return ResultUtils.success(shotService.getShotVO(shot));
    }
    @AuthCheck
    @PostMapping("/remove")
    public BaseResponse<Boolean> remove(@RequestBody DeleteRequest request) {
        shotService.deleteShot(request);
        return ResultUtils.success(true);
    }

}

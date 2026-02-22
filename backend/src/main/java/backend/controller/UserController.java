package backend.controller;

import backend.common.BaseResponse;
import backend.common.ResultUtils;
import backend.model.dto.user.UserLoginRequest;
import backend.model.dto.user.UserRegisterRequest;
import backend.model.entity.User;
import backend.model.vo.UserVO;
import backend.service.UserService;
import com.mybatisflex.core.paginate.Page;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户 控制层。
 *
 * @author <a href="https://github.com/Polarisyo">Polaris</a>
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 注册用户。
     *
     * @param request 用户
     * @return {@code true} 保存成功，{@code false} 保存失败
     */
    @PostMapping("register")
    public BaseResponse<Long> register(@RequestBody UserRegisterRequest request) {
        String userAccount = request.getUserAccount();
        String userPwd = request.getUserPassword();
        String checkPwd = request.getCheckPassword();
        long id = userService.userRegister(userAccount, userPwd, checkPwd);
        return ResultUtils.success(id);
    }

    @PostMapping("login")
    public BaseResponse<UserVO> login(@RequestBody UserLoginRequest req, HttpServletRequest request) {
        String userAccount = req.getUserAccount();
        String userPwd = req.getUserPassword();
        UserVO userVO = userService.userLogin(userAccount, userPwd, request);
        return ResultUtils.success(userVO);
    }
    @GetMapping("/get/login")
    public BaseResponse<UserVO> getLoginUser(HttpServletRequest request) {
        UserVO loginUser = userService.getLoginUser(request);
        return ResultUtils.success(loginUser);
    }

    /**
     * 根据主键删除用户。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Long id) {
        return userService.removeById(id);
    }

    /**
     * 根据主键更新用户。
     *
     * @param user 用户
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody User user) {
        return userService.updateById(user);
    }

    /**
     * 查询所有用户。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<User> list() {
        return userService.list();
    }

    /**
     * 根据主键获取用户。
     *
     * @param id 用户主键
     * @return 用户详情
     */
    @GetMapping("getInfo/{id}")
    public User getInfo(@PathVariable Long id) {
        return userService.getById(id);
    }

    /**
     * 分页查询用户。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<User> page(Page<User> page) {
        return userService.page(page);
    }

}

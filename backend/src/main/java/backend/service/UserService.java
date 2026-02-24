package backend.service;

import backend.model.dto.user.UserEditRequest;
import backend.model.vo.UserVO;
import com.mybatisflex.core.service.IService;
import backend.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 用户 服务层。
 *
 * @author <a href="https://github.com/Polarisyo">Polaris</a>
 */
public interface UserService extends IService<User> {

    long userRegister(String userAccount, String userPwd, String checkPwd);

    UserVO userLogin(String userAccount, String userPwd, HttpServletRequest request);

    UserVO getUserVO(User user);

    UserVO getLoginUser(HttpServletRequest request);
    UserVO updateUserInfo(UserEditRequest request);

}

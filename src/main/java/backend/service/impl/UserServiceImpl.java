package backend.service.impl;
import java.nio.charset.StandardCharsets;
import java.util.List;

import backend.exception.ErrorCode;
import backend.exception.ThrowUtils;
import backend.model.enums.UserPlanEnum;
import backend.model.enums.UserRoleEnum;
import backend.model.vo.UserVO;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.query.QueryCondition;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import backend.model.entity.User;
import backend.mapper.UserMapper;
import backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
 * 用户 服务层实现。
 *
 * @author <a href="https://github.com/Polarisyo">Polaris</a>
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>  implements UserService{

    /**
     * 验证输入信息，校验是否存在，加密密码，储存到数据库
     * @param userAccount
     * @param userPwd
     * @param checkPwd
     * @return
     */
    @Override
    public long userRegister(String userAccount, String userPwd, String checkPwd) {
        userAccountAndPwdValid(userAccount, userPwd);
        ThrowUtils.throwIf(!StrUtil.equals(userPwd, checkPwd), ErrorCode.PARAMS_ERROR);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userAccount", userAccount);
        long count = this.getMapper().selectCountByQuery(queryWrapper);
        ThrowUtils.throwIf(count != 0, ErrorCode.PARAMS_ERROR);

        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(getEncryptPwd(userPwd));
        user.setUserRole(UserRoleEnum.USER.getValue());
        user.setSubscriptionPlan(UserPlanEnum.FREE.getValue());
        user.setAiCredits(10);

        boolean save = this.save(user);
        ThrowUtils.throwIf(!save, ErrorCode.SYSTEM_ERROR);
        return user.getId();
    }

    private static void userAccountAndPwdValid(String userAccount, String userPwd) {
        ThrowUtils.throwIf(!StrUtil.isAllNotBlank(userAccount, userPwd), ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(userAccount.length() < 4 || userAccount.length() > 30
                || userPwd.length() < 4 || userPwd.length() > 30, ErrorCode.PARAMS_ERROR);

    }

    @Override
    public UserVO userLogin(String userAccount, String userPwd, HttpServletRequest request) {
        userAccountAndPwdValid(userAccount, userPwd);

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("userAccount", userAccount);
        wrapper.eq("userPassword", getEncryptPwd(userPwd));
        List<User> users = this.getMapper().selectListByQuery(wrapper);
        ThrowUtils.throwIf(users.size() != 1, ErrorCode.PARAMS_ERROR, "账户不存在或者密码错误");

        return getUserVO(users.getFirst());
    }

    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtil.copyProperties(user, userVO);
        return userVO;
    }

    public String getEncryptPwd(String pwd) {
        final String SALT = "SCRIPT";
        return DigestUtils.md5DigestAsHex((SALT + pwd).getBytes(StandardCharsets.UTF_8));
    }
}

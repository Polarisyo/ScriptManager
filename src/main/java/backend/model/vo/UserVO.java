package backend.model.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 用户 实体类。
 *
 */
@Data
public class UserVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */

    private Long id;

    /**
     * 账号
     */

    private String userAccount;

    /**
     * 用户昵称
     */

    private String userName;

    /**
     * 用户头像
     */

    private String userAvatar;

    /**
     * 用户简介
     */

    private String userProfile;

    /**
     * 用户角色：user/admin
     */

    private String userRole;

    /**
     * 订阅计划:free/basic/pro/enterprise
     */

    private String subscriptionPlan;

    /**
     * AI积分
     */

    private Integer aiCredits;

    /**
     * 最后登录时间
     */

    private Timestamp lastLoginTime;
}

package backend.model.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 用户 实体类。
 *
 */
@Data
public class UserEditRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

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
}

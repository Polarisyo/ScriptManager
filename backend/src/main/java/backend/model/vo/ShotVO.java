package backend.model.vo;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * 分镜主表 实体类。
 *

 */
@Data
public class ShotVO implements Serializable {


    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 分镜ID，主键
     */

    private Long shotId;

    /**
     * 所属项目ID
     */

    private Long projectId;

    /**
     * 分镜标题
     */

    private String shotTitle;

    /**
     * 分镜描述
     */

    private String shotDesc;

    /**
     * 分镜时长（秒）
     */
    private Integer duration;

    /**
     * 排序序号（用于拖拽排序）
     */

    private Integer orderIndex;

    /**
     * 分镜状态：draft(草稿)/inProgress(进行中)/completed(已完成)
     */

    private String shotStatus;

    /**
     * 分镜对应剧本内容
     */
    private String scriptContent;
}

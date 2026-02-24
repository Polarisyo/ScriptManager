package backend.model.dto.shot;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;


/**
 * 分镜主表 实体类。
 *

 */
@Data
public class ShotAddRequest implements Serializable {


    @Serial
    private static final long serialVersionUID = 1L;

    private Long userId;
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
    /**
     * 分镜顺序
     */
    private Integer[] shotOrder;
}

package backend.controller;

import com.mybatisflex.core.paginate.Page;
import backend.model.entity.ShotMaterial;
import backend.service.ShotMaterialService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分镜素材关联表 控制层。
 *
 * @author <a href="https://github.com/Polarisyo">Polaris</a>
 */
@RestController
@RequestMapping("/shotMaterial")
public class ShotMaterialController {

    @Resource
    private ShotMaterialService shotMaterialService;

    /**
     * 保存分镜素材关联表。
     *
     * @param shotMaterial 分镜素材关联表
     * @return {@code true} 保存成功，{@code false} 保存失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody ShotMaterial shotMaterial) {
        return shotMaterialService.save(shotMaterial);
    }

    /**
     * 根据主键删除分镜素材关联表。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Long id) {
        return shotMaterialService.removeById(id);
    }

    /**
     * 根据主键更新分镜素材关联表。
     *
     * @param shotMaterial 分镜素材关联表
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody ShotMaterial shotMaterial) {
        return shotMaterialService.updateById(shotMaterial);
    }

    /**
     * 查询所有分镜素材关联表。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<ShotMaterial> list() {
        return shotMaterialService.list();
    }

    /**
     * 根据主键获取分镜素材关联表。
     *
     * @param id 分镜素材关联表主键
     * @return 分镜素材关联表详情
     */
    @GetMapping("getInfo/{id}")
    public ShotMaterial getInfo(@PathVariable Long id) {
        return shotMaterialService.getById(id);
    }

    /**
     * 分页查询分镜素材关联表。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<ShotMaterial> page(Page<ShotMaterial> page) {
        return shotMaterialService.page(page);
    }

}

package com.wms.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.demo.common.QueryPageData;
import com.wms.demo.common.Result;
import com.wms.demo.entity.Storage;
import com.wms.demo.service.IStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 库存物品表 前端控制器
 * </p>
 *
 * @author pengbo
 * @since 2026-04-01
 */
@RestController
@CrossOrigin
@RequestMapping("/storage")
public class StorageController {

    @Autowired
    private IStorageService storageService;

    @GetMapping("/list")
    public Result list() {
        return Result.success(storageService.list());
    }

    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageData query) {
        Page<Storage> page = new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());
        String itemName = (String) query.getData().get("itemName");
        String itemId = (String) query.getData().get("itemId");

        LambdaQueryWrapper<Storage> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (itemName != null && !itemName.isEmpty()) {
            lambdaQueryWrapper.like(Storage::getItemName, itemName);
        }
        if (itemId != null && !itemId.isEmpty()) {
            lambdaQueryWrapper.like(Storage::getItemId, itemId);
        }

        IPage<Storage> result = storageService.page(page, lambdaQueryWrapper);
        return Result.success(result.getRecords(), result.getTotal());
    }

    @PostMapping("/save")
    public Result save(@RequestBody Storage storage) {
        try {
            return storageService.save(storage) ? Result.success() : Result.error("保存失败");
        } catch (org.springframework.dao.DuplicateKeyException e) {
            return Result.error("物品编号已存在");
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            return Result.error("数据不合法/违反约束");
        } catch (Exception e) {
            return Result.error("系统异常");
        }
    }

    @PostMapping("/updateById")
    public boolean updateById(@RequestBody Storage storage) {
        return storageService.updateById(storage);
    }

    @GetMapping("/delete")
    public boolean delete(@RequestParam Long id) {
        return storageService.removeById(id);
    }
}

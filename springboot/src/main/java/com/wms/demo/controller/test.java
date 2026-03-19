package com.wms.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.demo.common.QueryPageData;
import com.wms.demo.common.Result;
import com.wms.demo.entity.User;
import com.wms.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class test {

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public Result list(){
        return Result.success(userService.list());
    }

    // 模糊搜索
    @PostMapping("/listFuzzyByName")
    public List<User> listFuzzyByName(@RequestBody User user){
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.like(User::getName, user.getName());
        return userService.list(lambdaQueryWrapper);
    }

    // 分页
    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageData query){
        Page<User> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());
        String name = (String)query.getData().get("name");
        String sex = (String)query.getData().get("sex");

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper();
        if(name != null && !name.isEmpty()){
            lambdaQueryWrapper.like(User::getName, name);
        }
        if(sex != null  && !sex.isEmpty()){
            lambdaQueryWrapper.like(User::getSex, Integer.valueOf(sex));
        }

        IPage result = userService.page(page, lambdaQueryWrapper);

        return Result.success(result.getRecords(), result.getTotal());
    }

    // 新增
    @PostMapping("/save")
    public Result save(@RequestBody User user){
        user.setPassword("password");
        try {
            return userService.save(user) ? Result.success() : Result.error("保存失败");
        } catch (org.springframework.dao.DuplicateKeyException e) {
            return Result.error("账号已存在");
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            return Result.error("数据不合法/违反约束");
        } catch (Exception e) {
            return Result.error("系统异常");
        }
    }

    // 修改
    @PostMapping("/updateById")
    public boolean updateById(@RequestBody User user){
        return userService.updateById(user);
    }

    // 删除
    @GetMapping("/delete")
    public boolean delete(@RequestParam Integer id){
        return userService.removeById(id);
    }
}

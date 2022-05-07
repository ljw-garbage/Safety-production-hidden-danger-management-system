package com.example.demo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.example.demo.service.IGuidanceService;
import com.example.demo.entity.Guidance;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ljw
 * @since 2022-04-18
 */
@RestController
@RequestMapping("/guidance")
        public class GuidanceController {
    
    @Resource
    private IGuidanceService guidanceService;

        @PostMapping
        public Result save(@RequestBody Guidance guidance) {
            return Result.success(guidanceService.saveOrUpdate(guidance));
        }

        @DeleteMapping("/{id}")
        public Result delete(@PathVariable Integer id) {
            return Result.success(guidanceService.removeById(id));
        }
        @DeleteMapping("/del/batch")
        public Result deleteBath(@RequestBody List<Integer> ids) {
            return Result.success(guidanceService.removeByIds(ids));
        }

        @GetMapping
        public Result findAll() {
            return Result.success(guidanceService.list());
        }

        @GetMapping("/{id}")
        public Result findOne(@PathVariable Integer id) {
            return Result.success(guidanceService.getById(id));
        }

        @GetMapping("/page")
        public Result findPage(@RequestParam Integer pageNum,
                                        @RequestParam Integer pageSize) {
            QueryWrapper<Guidance> queryWrapper = new QueryWrapper<>();
            queryWrapper.orderByDesc("id");
            return Result.success(guidanceService.page(new Page<>(pageNum, pageSize),queryWrapper));
        }
}


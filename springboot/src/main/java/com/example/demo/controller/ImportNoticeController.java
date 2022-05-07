package com.example.demo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.example.demo.service.IImportNoticeService;
import com.example.demo.entity.ImportNotice;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ljw
 * @since 2022-04-18
 */
@RestController
@RequestMapping("/import-notice")
        public class ImportNoticeController {
    
    @Resource
    private IImportNoticeService importNoticeService;

        @PostMapping
        public Result save(@RequestBody ImportNotice importNotice) {
            return Result.success(importNoticeService.saveOrUpdate(importNotice));
        }

        @DeleteMapping("/{id}")
        public Result delete(@PathVariable Integer id) {
            return Result.success(importNoticeService.removeById(id));
        }
        @DeleteMapping("/del/batch")
        public Result deleteBath(@RequestBody List<Integer> ids) {
            return Result.success(importNoticeService.removeByIds(ids));
        }

        @GetMapping
        public Result findAll() {
            return Result.success(importNoticeService.list());
        }

        @GetMapping("/{id}")
        public Result findOne(@PathVariable Integer id) {
            return Result.success(importNoticeService.getById(id));
        }

        @GetMapping("/page")
        public Result findPage(@RequestParam Integer pageNum,
                                        @RequestParam Integer pageSize) {
            QueryWrapper<ImportNotice> queryWrapper = new QueryWrapper<>();
            queryWrapper.orderByDesc("id");
            return Result.success(importNoticeService.page(new Page<>(pageNum, pageSize),queryWrapper));
        }
}


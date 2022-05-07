package com.example.demo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.example.demo.service.IInstructionsHelpService;
import com.example.demo.entity.InstructionsHelp;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ljw
 * @since 2022-04-16
 */
@RestController
@RequestMapping("/instructions-help")
        public class InstructionsHelpController {
    
    @Resource
    private IInstructionsHelpService instructionsHelpService;

        @PostMapping
        public Result save(@RequestBody InstructionsHelp instructionsHelp) {
            return Result.success(instructionsHelpService.saveOrUpdate(instructionsHelp));
        }

        @DeleteMapping("/{id}")
        public Result delete(@PathVariable Integer id) {
            return Result.success(instructionsHelpService.removeById(id));
        }
        @DeleteMapping("/del/batch")
        public Result deleteBath(@RequestBody List<Integer> ids) {
            return Result.success(instructionsHelpService.removeByIds(ids));
        }

        @GetMapping
        public Result findAll() {
            return Result.success(instructionsHelpService.list());
        }

        @GetMapping("/{id}")
        public Result findOne(@PathVariable Integer id) {
            return Result.success(instructionsHelpService.getById(id));
        }

        @GetMapping("/page")
        public Result findPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize) {
            QueryWrapper<InstructionsHelp> queryWrapper = new QueryWrapper<>();
            queryWrapper.orderByDesc("id");
            return Result.success(instructionsHelpService.page(new Page<>(pageNum, pageSize),queryWrapper));
        }
}


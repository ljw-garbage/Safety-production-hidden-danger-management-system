package com.example.demo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.example.demo.service.IFeedbackService;
import com.example.demo.entity.Feedback;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ljw
 * @since 2022-03-17
 */
@RestController
@RequestMapping("/feedback")
        public class FeedbackController {
    
    @Resource
    private IFeedbackService feedbackService;

        @PostMapping
        public Result save(@RequestBody Feedback feedback) {
            return Result.success(feedbackService.saveOrUpdate(feedback));
        }

        @DeleteMapping("/{id}")
        public Result delete(@PathVariable Integer id) {
            return Result.success(feedbackService.removeById(id));
        }
        @DeleteMapping("/del/batch")
        public Result deleteBath(@RequestBody List<Integer> ids) {
            return Result.success(feedbackService.removeByIds(ids));
        }

        @GetMapping
        public Result findAll() {
            return Result.success(feedbackService.list());
        }

        @GetMapping("/{id}")
        public Result findOne(@PathVariable Integer id) {
            return Result.success(feedbackService.getById(id));
        }

        @GetMapping("/page")
        public Result findPage(@RequestParam Integer pageNum,
                                        @RequestParam Integer pageSize) {
            QueryWrapper<Feedback> queryWrapper = new QueryWrapper<>();
            queryWrapper.orderByDesc("id");
            return Result.success(feedbackService.page(new Page<>(pageNum, pageSize),queryWrapper));
    }
}


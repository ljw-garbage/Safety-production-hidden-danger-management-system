package com.example.demo.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.utils.TokenUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.example.demo.service.ICommentService;
import com.example.demo.entity.Comment;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ljw
 * @since 2022-04-18
 */
@RestController
@RequestMapping("/comment")
        public class CommentController {
    
    @Resource
    private ICommentService commentService;

        @PostMapping
        public Result save(@RequestBody Comment comment) {
            if(comment.getId()==null){
                comment.setUserId(TokenUtils.getCurrentUser().getId());
                comment.setTime(DateUtil.now());
            }
            return Result.success(commentService.saveOrUpdate(comment));
        }

        @DeleteMapping("/{id}")
        public Result delete(@PathVariable Integer id) {
            return Result.success(commentService.removeById(id));
        }
        @DeleteMapping("/del/batch")
        public Result deleteBath(@RequestBody List<Integer> ids) {
            return Result.success(commentService.removeByIds(ids));
        }

        @GetMapping
        public Result findAll() {
            return Result.success(commentService.list());
        }

        @GetMapping("/tree/{reportId}")
        public Result findTree(@PathVariable Integer reportId) {
            List<Comment> Comments = commentService.findCommentDetail(reportId);
            return Result.success(Comments);
        }

        @GetMapping("/{id}")
        public Result findOne(@PathVariable Integer id) {
            return Result.success(commentService.getById(id));
        }

        @GetMapping("/page")
        public Result findPage(@RequestParam Integer pageNum,
                                        @RequestParam Integer pageSize) {
            QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
            queryWrapper.orderByDesc("id");
            return Result.success(commentService.page(new Page<>(pageNum, pageSize),queryWrapper));
        }
}


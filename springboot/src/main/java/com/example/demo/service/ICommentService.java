package com.example.demo.service;

import com.example.demo.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.mapper.CommentMapper;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ljw
 * @since 2022-04-18
 */
public interface ICommentService extends IService<Comment> {

    List<Comment> findCommentDetail(Integer reportId);
}

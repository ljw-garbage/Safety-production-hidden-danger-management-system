package com.example.demo.service.impl;

import com.example.demo.entity.Feedback;
import com.example.demo.mapper.FeedbackMapper;
import com.example.demo.service.IFeedbackService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ljw
 * @since 2022-03-17
 */
@Service
public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper, Feedback> implements IFeedbackService {

}

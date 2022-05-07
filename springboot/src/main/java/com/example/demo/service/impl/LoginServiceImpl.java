package com.example.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.common.Constants;
import com.example.demo.entity.LoginForm;
import com.example.demo.entity.User;
import com.example.demo.exception.ServiceException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.LoginService;

import com.example.demo.utils.JwtUtil;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl extends ServiceImpl<UserMapper, User> implements  LoginService {
    private static final Log LOG = Log.get();
    @Override
    public LoginForm login(LoginForm loginForm) {
        User one = getUserInfo(loginForm);
        if(one!=null){
            BeanUtil.copyProperties(one,loginForm,true);
            //设置token
            String token = JwtUtil.genToken(one.getId().toString(), one.getPassword());
            loginForm.setToken(token);
            return loginForm;
        }else throw new ServiceException(Constants.code_600, "用户名或密码错误");
    }
    private User getUserInfo(LoginForm loginForm){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", loginForm.getUsername());
        queryWrapper.eq("password", loginForm.getPassword());
//        queryWrapper.eq("captcha", loginForm.getCaptcha());
        User one;
        // 处理异常情况
        try {
            one = getOne(queryWrapper);
        } catch (Exception e) {
            LOG.error(e);
            throw  new ServiceException(Constants.code_500,"系统错误");
        }
        return one;
    }
}

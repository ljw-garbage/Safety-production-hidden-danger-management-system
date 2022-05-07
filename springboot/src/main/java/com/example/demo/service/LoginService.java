package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.LoginForm;
import com.example.demo.entity.User;


public interface LoginService extends IService<User> {
    LoginForm login(LoginForm loginForm);
}
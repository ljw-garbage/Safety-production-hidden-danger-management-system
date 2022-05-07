package com.example.demo.controller.dto;

import cn.hutool.core.util.StrUtil;
import com.example.demo.common.Constants;
import com.example.demo.common.Result;
import com.example.demo.entity.LoginForm;
import com.example.demo.service.LoginService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/app")
public class LoginController {

    @Resource
    LoginService loginService;
    /**
     * 登录接口
     * @return
     */
    @PostMapping("/login")
    public Result login(@RequestBody LoginForm loginForm) {
        String username = loginForm.getUsername();
        String password = loginForm.getPassword();
        String captcha=loginForm.getCaptcha();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)||StrUtil.isBlank(captcha)) {
            return Result.error(Constants.code_400,"参数错误");
        }
        LoginForm dto = loginService.login(loginForm);
        return Result.success(dto);
    }


}

package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.dto.UserDTO;
import com.example.demo.dto.UserPasswordDto;
import com.example.demo.entity.User;

public interface IUserService extends IService<User> {
    UserDTO login(UserDTO userDTO);
    boolean saveUser(User user);
    User register(UserDTO userDTO);

    void updatePassword(UserPasswordDto userPasswordDto);
}

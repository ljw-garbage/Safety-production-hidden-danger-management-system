//package com.example.demo.service;
//
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.example.demo.dto.UserDTO;
//import com.example.demo.enity.User;
//import com.example.demo.mapper.UserMapper;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.Resource;
//@Service
//public class UserService extends ServiceImpl<UserMapper,User> {
////    public boolean saveUser(User user) {
//////        if(user.getId()==null){ //user 没有id 则表示新增
//////            return save(user);
//////        } else { //否则表示更新
//////            return updateById(user);
//////        }
////        return saveOrUpdate(user);
////    }
//
////    public boolean login(UserDTO userDTO) {
////        return false;
////    }
////    @Resource
////    private UserMapper userMapper;
////
////    public int save(User user){
////        if(user.getId()==null){ //user 没有id 则表示新增
////            return userMapper.insert(user);
////        }else{ //否则表示更新
////            return userMapper.update(user);
////        }
////    }
//}

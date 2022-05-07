package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.dto.UserPasswordDto;
import com.example.demo.entity.User;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends BaseMapper<User> {
//    @Select("select * from user")
//    List<User> findAll();
//
////    @Insert("INSERT into user(username,password,account,territory ,phone) VALUES (#{username},#{password},#{account},#{territory},#{phone})");
//    @Insert("INSERT INTO USER(username,password,account,territory,phone) VALUES(#{username},#{password},#{account},#{territory},#{phone})")
//    int insert(User user);
//
////    @Update("update user set username=#{username},password=#{password},account=#{account},territory=#{territory},phone=#{phone} where id=#{id}")
//    int update(User user);
//
//    @Delete("delete  from user where id=#{id}")
//    Integer deleteById(Integer id);
//
//    @Select("select * from user  where username like concat('%',#{username},'%')  limit #{pageNum},#{pageSize}")
//    List<User> selectPage(Integer pageNum,Integer pageSize,String username);
//
//    @Select("select count(*) from user where username like concat('%',#{username},'%')")
//    Integer selectTotal(String username);
    @Update("update user set password = #{newPassword} where username = #{username} and password = #{password}")
    int updatePassword(UserPasswordDto userPasswordDto);
}

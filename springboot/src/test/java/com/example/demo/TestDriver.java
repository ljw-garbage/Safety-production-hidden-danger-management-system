package com.example.demo;

import com.example.demo.entity.User;
import com.example.demo.service.IUserService;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class TestDriver {

    @Resource
    IUserService userService;
    @Test
    public void test01() {
        {
            // 推荐
            String resource = "mybatis-config.xml";
            try {
                InputStream inputStream = Resources.getResourceAsStream(resource);
                // 创建了mybatis的sqlSessionFactory工厂类:读取mybatis.xml配置文件，使用该配置文件中的参数信息，用于创建SqlSession对象
                SqlSessionFactory sqlSessionFactory =
                        new SqlSessionFactoryBuilder().build(inputStream);
                // 获取一个SqlSession对象进行数据库操作
                // SqlSession对象等价于 jdbc 的 statement；或者是 dbutils 的 QueryRunner
                SqlSession session = sqlSessionFactory.openSession();
                // 从mybatis的sqlsession对象中获取dao层的接口的实例对象
                User userInfoBeanM = session.getMapper(User.class);
                // 调用dao层接口的方法进行数据库操作
                List<User> users = userService.list();
                for (User str : users) {
                    System.out.println(str.getUsername()+" "+str.getPassword()+"\n");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

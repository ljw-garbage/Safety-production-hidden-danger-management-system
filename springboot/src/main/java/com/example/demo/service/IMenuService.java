package com.example.demo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ljw
 * @since 2022-02-24
 */
public interface IMenuService extends IService<Menu> {

    List<Menu> findMenus(String name);
//    Page<Menu> findPage(Page<Menu> objectPage,String name);
}

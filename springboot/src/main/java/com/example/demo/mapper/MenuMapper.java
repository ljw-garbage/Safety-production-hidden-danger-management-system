package com.example.demo.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ljw
 * @since 2022-02-24
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {
//    Page<Menu> findPage(Page<Menu> page, @Param("name") String name);
}

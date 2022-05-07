package com.example.demo.mapper;

import com.example.demo.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ljw
 * @since 2022-02-24
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    @Select("select id from  role where flag=#{flag}")
    Integer selectByFlag(@Param("flag") String role);
}

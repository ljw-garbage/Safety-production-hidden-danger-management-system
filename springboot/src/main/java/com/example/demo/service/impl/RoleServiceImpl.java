package com.example.demo.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.Menu;
import com.example.demo.entity.Role;
import com.example.demo.entity.RoleMenu;
import com.example.demo.mapper.RoleMapper;
import com.example.demo.mapper.RoleMenuMapper;
import com.example.demo.service.IMenuService;
import com.example.demo.service.IRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ljw
 * @since 2022-02-24
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {
    @Resource
    private RoleMenuMapper roleMenuMapper;
    @Resource
    private IMenuService menuService;
    @Transactional
    @Override
    public void setRoleMenu(Integer roleId, List<Integer> menuIds) {
        //先删除所有当前角色id的所有关系
//        QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("role_id",roleId);
//        roleMenuMapper.delete(queryWrapper);
        roleMenuMapper.deleteByRoleId(roleId);
        //再把前端传过来的角色菜单绑定到当前角色上
        List<Integer> menuIdsCopy= CollUtil.newArrayList(menuIds);
        for (Integer menuId : menuIds) {
            Menu menu = menuService.getById(menuId);
            if (menu.getPid()!=null && !menuIdsCopy.contains(menu.getPid())){ //二级菜单，并且传过来的menuId 里面没有父级id
                //补上父级id
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setMenuId(menu.getPid());
                roleMenu.setRoleId(roleId);
                roleMenuMapper.insert(roleMenu);
                menuIdsCopy.add(menu.getPid());
            }

            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setMenuId(menuId);
            roleMenu.setRoleId(roleId);
            roleMenuMapper.insert(roleMenu);
        }
    }

    @Override
    public List<Integer> getRoleMenu(Integer roleId) {
//        QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("role_id",roleId);
//        return roleMenuMapper.selectById(queryWrapper);

          return roleMenuMapper.selectByRoleId(roleId);
    }

}

package com.example.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.common.Constants;
import com.example.demo.common.RoleEnum;
import com.example.demo.dto.UserDTO;
import com.example.demo.dto.UserPasswordDto;
import com.example.demo.entity.Menu;
import com.example.demo.entity.User;
import com.example.demo.exception.ServiceException;
import com.example.demo.mapper.MenuMapper;
import com.example.demo.mapper.RoleMapper;
import com.example.demo.mapper.RoleMenuMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.IMenuService;
import com.example.demo.service.IUserService;
import com.example.demo.utils.TokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    private static final Log LOG = Log.get();

    @Resource
    private RoleMapper roleMapper;//导入角色表

    @Resource
    private RoleMenuMapper roleMenuMapper;//导入关联关系表

    @Resource
    private IMenuService menuService;

    @Resource
    private UserMapper userMapper;

    @Override
    public UserDTO login(UserDTO userDTO) {
        User one = getUserInfo(userDTO);
        if(one!=null){
            BeanUtil.copyProperties(one,userDTO,true);
            //设置token
            String token = TokenUtils.genToken(one.getId().toString(), one.getPassword());
            userDTO.setToken(token);
            //获取所有用户信息
            String role = one.getRole();    //ROLE_ADMIN
            //设置用户的菜单列表
            List<Menu> roleMenus = getRoleMenus(role);
            userDTO.setMenus(roleMenus);
            return userDTO;
        }else throw new ServiceException(Constants.code_600, "用户名或密码错误");
    }

    @Override
    public boolean saveUser(User user) {
        return saveOrUpdate(user);
    }

    @Override
    public User register(UserDTO userDTO) {
        User one = getUserInfo(userDTO);
        if(one==null){
            one=new User();
            BeanUtil.copyProperties(userDTO,one,true);
            //默认一个普通用户的角色
            one.setRole(RoleEnum.ROLE_USER.toString());
            save(one);
        }else{
            throw new ServiceException(Constants.code_600, "用户已存在");
        }
        return one;
    }

    @Override
    public void updatePassword(UserPasswordDto userPasswordDto) {
        int update = userMapper.updatePassword(userPasswordDto);
        if (update < 1) {
            throw new ServiceException(Constants.code_400, "密码错误");
        }
    }

    private User getUserInfo(UserDTO userDTO){
       QueryWrapper<User> queryWrapper = new QueryWrapper<>();
       queryWrapper.eq("username", userDTO.getUsername());
       queryWrapper.eq("password", userDTO.getPassword());
       User one;
       // 处理异常情况
       try {
           one = getOne(queryWrapper);
       } catch (Exception e) {
           LOG.error(e);
           throw  new ServiceException(Constants.code_500,"系统错误");
       }
       return one;
   }

    /**
     * 获取当前角色的菜单列表
     * @param roleFlag
     * @return
     */
   private List<Menu> getRoleMenus(String roleFlag){
       Integer roleId = roleMapper.selectByFlag(roleFlag); //查出当前用户的roleId
       List<Integer> menuIds = roleMenuMapper.selectByRoleId(roleId); //根据id 查询出所有的Ids
       List<Menu> menus = menuService.findMenus(""); //查出所有的菜单
       //new 一个删选之后的菜单
       List<Menu> roleMenus = new ArrayList<>();
       //筛选当前用户的角色菜单
       for (Menu menu : menus) {
           if(menuIds.contains(menu.getId())){
               roleMenus.add(menu);
           }
           List<Menu> children = menu.getChildren();
           //removeIf  移除children 里不在menuIds 集合中的 元素
           children.removeIf(child->!menuIds.contains(child.getId()));
       }
        return roleMenus;
   }
}

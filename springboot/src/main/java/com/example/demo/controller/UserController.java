package com.example.demo.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Constants;
import com.example.demo.common.Result;
import com.example.demo.dto.UserDTO;
import com.example.demo.dto.UserPasswordDto;
import com.example.demo.entity.User;
import com.example.demo.service.IUserService;
import com.example.demo.utils.TokenUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    IUserService userService;
//    新增修改
    @PostMapping
    public Result save(@RequestBody User user){
        return Result.success(userService.saveUser(user));
    }
//    @GetMapping
//    public Result<?> findPage(@RequestParam Integer pageNum,@RequestParam Integer pageSize,@RequestParam String search){
//        Page<User> userPage=userMapper.selectPage(new Page<>(pageNum,pageSize), Wrappers.<User>lambdaQuery().like(User::getAccount,search));
//        return Result.success(userPage);
//    }
//    查询
    @GetMapping
    public Result findAll(){
      return Result.success(userService.list()) ;
    }
    //删除
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        return Result.success(userService.removeById(id));
    }

    //批量删除
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        return Result.success(userService.removeByIds(ids));
    }
    //分页查询
//    @GetMapping("/page")
//    public Map<String, Object> findPage(@RequestParam Integer pageNum,
//                                        @RequestParam Integer pageSize,
//                                        @RequestParam(defaultValue = "") String username){
//        pageNum=(pageNum-1)*pageSize;
//        List<User> data=userMapper.selectPage(pageNum,pageSize,username);
//        Map<String, Object> res = new HashMap<>();
//        Integer total = userMapper.selectTotal(username);
//        res.put("data",data);
//        res.put("total",total);
//        return res;
//    }

    //分页查询
    //mybatis-plus 方式
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize,
                                @RequestParam(defaultValue = "") String username,
                                @RequestParam(defaultValue = "") String territory,
                                @RequestParam(defaultValue = "") String account){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if(!"".equals(username)){
            queryWrapper.like("username",username);
        }
        if(!"".equals(territory)){
            queryWrapper.like("territory",territory);
        }
        if(!"".equals(account)){
            queryWrapper.like("account",account);
        }
        User currentUser = TokenUtils.getCurrentUser();
        System.out.println("获取当前用户信息："+currentUser.getAccount());
        return Result.success(userService.page(new Page<>(pageNum,pageSize),queryWrapper)) ;
    }

    /**
     * 导出接口
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception{
        //从数据库中查询所有的数据
        List<User> list=userService.list();
        //在内存操作,写到浏览器
        ExcelWriter writer= ExcelUtil.getWriter(true);
        //自定义表头标题别名
        writer.addHeaderAlias("username","用户名");
        writer.addHeaderAlias("password","密码");
        writer.addHeaderAlias("account","账号名");
        writer.addHeaderAlias("territory","负责区域");
        writer.addHeaderAlias("assessTime","创建时间");
        writer.addHeaderAlias("phone","手机号");
        writer.addHeaderAlias("avatarUrl","头像");

        //一次性写出list 对象到excel,使用默认样式,强制输出标题
        writer.write(list,true);
        //设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName= URLEncoder.encode("用户信息","utf-8");
        response.setHeader("Content-Disposition","attachment;filename="+fileName+".xlsx");

        ServletOutputStream out=response.getOutputStream();
        writer.flush(out, true);
        writer.close();
        IoUtil.close(out);
    }

    /**
     * 导入接口
     */
    @PostMapping("/import")
    public Result imp(MultipartFile file) throws Exception{
        InputStream inputStream=file.getInputStream();
        ExcelReader reader=ExcelUtil.getReader(inputStream);
        //通过 javaBean 的方式读取Excel 内对象
//        List<User> list=reader.readAll(User.class);

        //忽略表头中文
        List<List<Object>> list = reader.read(1);
        List<User> users = CollUtil.newArrayList();
        for(List<Object> row:list){
            User user=new User();
            user.setUsername(row.get(0).toString());
            user.setPassword(row.get(1).toString());
            user.setAccount(row.get(2).toString());
            user.setTerritory(row.get(3).toString());
            user.setPhone(row.get(4).toString());
            user.setAvatarUrl(row.get(5).toString());
            users.add(user);
        }
        userService.saveBatch(users);
        return Result.success(true);
    }
    /**
     * 登录接口
     * @return
     */
    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO) {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error(Constants.code_400,"参数错误");
        }
        UserDTO dto = userService.login(userDTO);
        return Result.success(dto);
    }
    /**
     *注册接口
     *
     */
    @PostMapping("/register")
    public Result register(@RequestBody UserDTO userDTO) {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error(Constants.code_400,"参数错误");
        }
        return Result.success(userService.register(userDTO));
    }
    /**
     *通过用户名查询用户
     *
     */
    @GetMapping("/username/{username}")
    public Result findOne(@PathVariable String username){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //select * from user where username=#{username}
        queryWrapper.eq("username",username);
        return Result.success(userService.getOne(queryWrapper));
    }
    /**
     * 修改密码
     * @param userPasswordDto
     * @return
     */
    @PostMapping("/password")
    public Result password(@RequestBody UserPasswordDto userPasswordDto) {
        userService.updatePassword(userPasswordDto);
        return Result.success();
    }
}

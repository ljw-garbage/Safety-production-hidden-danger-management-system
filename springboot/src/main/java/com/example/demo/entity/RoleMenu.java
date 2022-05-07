package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
@TableName("role_menu")
@Data
public class RoleMenu {
    private Integer roleId;
    private Integer menuId;
}

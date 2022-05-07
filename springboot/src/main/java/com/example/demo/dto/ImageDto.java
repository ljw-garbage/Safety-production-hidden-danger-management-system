package com.example.demo.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("image")
public class ImageDto {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField(value = "icon_url")
    private String iconUrl;
    @TableField(value = "icon_text")
    private String iconText;
    private String path;
}

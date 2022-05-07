package com.example.demo.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
@TableName("upload_image")
@Data
public class UploadImageDto {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String file;
    private String status;
    private String content;
    private String message;
}

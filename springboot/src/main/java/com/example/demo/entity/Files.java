package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
@TableName("file")
@Data
public class Files {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private Long size;
    private String type;
    private String url;
    private String md5;
    private Boolean isDelete;
    private Boolean enable;

}

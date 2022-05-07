package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.demo.dto.UploadImageDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@TableName("report")
@Data
public class Report {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String address;
    private String territory;
    private String classification;
    @TableField(value = "enterprise_name")
    private String enterpriseName;
    @TableField(value = "contact_person")
    private String contactPerson;
    private String phone;
    private String detail;
    private String proposal;
    private String term;
    private Boolean enable;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "create_time")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "update_time")
    private Date updateTime;
    private String result;
    private String measures;
    @TableField(exist = false)
    private List<UploadImageDto> imageUrl;
}

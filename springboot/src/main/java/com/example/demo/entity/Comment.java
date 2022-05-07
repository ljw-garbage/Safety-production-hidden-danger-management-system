package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author ljw
 * @since 2022-04-18
 */
@Getter
@Setter
  @TableName("t_comment")
@ApiModel(value = "Comment对象", description = "")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;
      @TableId(value = "id", type = IdType.AUTO)
      @ApiModelProperty("id")
        private Integer id;

      @ApiModelProperty("内容")
      private String content;

      @ApiModelProperty("反馈人id")
      private Integer userId;

      @ApiModelProperty("反馈时间")
      private String time;

      @ApiModelProperty("父id")
      private Integer pid;

      @ApiModelProperty("祖宗id")
      private Integer originId;

      @ApiModelProperty("隐患信息报告id")
      private Integer reportId;

      @TableField(exist = false)
      private String account;

      @TableField(exist = false)
      private String avatarUrl;

}

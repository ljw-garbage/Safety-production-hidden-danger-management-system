package com.example.demo.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
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
  @ApiModel(value = "Guidance对象", description = "")
public class Guidance implements Serializable {

    private static final long serialVersionUID = 1L;

      @ApiModelProperty("id")
        private Integer id;

      @ApiModelProperty("标题")
      private String title;

      @ApiModelProperty("内容")
      private String content;

      @ApiModelProperty("日期")
      @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
      private Date date;


}

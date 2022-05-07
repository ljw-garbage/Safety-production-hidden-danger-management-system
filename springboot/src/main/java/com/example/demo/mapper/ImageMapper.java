package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.dto.ImageDto;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageMapper extends BaseMapper<ImageDto> {
}

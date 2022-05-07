package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.Files;
import org.springframework.stereotype.Repository;

@Repository
public interface FileMapper extends BaseMapper<Files> {
}

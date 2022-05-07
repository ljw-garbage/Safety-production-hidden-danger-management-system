package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.dto.ImageDto;
import com.example.demo.mapper.ImageMapper;
import com.example.demo.service.ImageService;
import org.springframework.stereotype.Service;

@Service
public class ImageServiceImpl extends ServiceImpl<ImageMapper, ImageDto> implements ImageService {
}

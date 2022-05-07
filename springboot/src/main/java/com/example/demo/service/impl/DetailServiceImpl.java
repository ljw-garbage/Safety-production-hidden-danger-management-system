package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.Detail;
import com.example.demo.mapper.DetailMapper;
import com.example.demo.service.DetailService;
import org.springframework.stereotype.Service;

@Service
public class DetailServiceImpl extends ServiceImpl<DetailMapper, Detail> implements DetailService {
    @Override
    public boolean saveDetail(Detail detail) {
        return saveOrUpdate(detail);
    }
}

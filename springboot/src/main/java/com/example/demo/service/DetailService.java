package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.Detail;
import com.example.demo.entity.User;

public interface DetailService extends IService<Detail> {
    boolean saveDetail(Detail detail);
}

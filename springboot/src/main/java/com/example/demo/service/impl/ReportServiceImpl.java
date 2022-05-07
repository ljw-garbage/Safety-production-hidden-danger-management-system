package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.Report;
import com.example.demo.mapper.ReportMapper;
import com.example.demo.service.ReportService;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl extends ServiceImpl<ReportMapper, Report> implements ReportService {

    @Override
    public boolean saveReport(Report report) {
        return saveOrUpdate(report);
    }
}

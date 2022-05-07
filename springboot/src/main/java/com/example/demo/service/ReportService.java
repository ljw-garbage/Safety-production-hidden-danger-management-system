package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.Report;

public interface ReportService extends IService<Report> {
    boolean saveReport(Report report);
}

package com.example.demo.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Constants;
import com.example.demo.common.Result;
import com.example.demo.entity.Report;
import com.example.demo.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/report")
public class ReportController {
    @Resource
    ReportService reportService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String enterpriseName,
                           @RequestParam(defaultValue = "") String classification,
                           @RequestParam(defaultValue = "") String result,
                           @RequestParam(defaultValue = "") String territory
    ){
        QueryWrapper<Report> queryWrapper = new QueryWrapper<>();
//        queryWrapper.orderByDesc("id");
        if(!"".equals(enterpriseName)){
            queryWrapper.like("enterpriseName",enterpriseName);
        }
        if(!"".equals(classification)){
            queryWrapper.like("classification",classification);
        }
        if(!"".equals(result)){
            queryWrapper.like("result",result);
        }
        if(!"".equals(territory)){
            queryWrapper.like("territory",territory);
        }
//        User currentUser = TokenUtils.getCurrentUser();
//        System.out.println("???????????????????????????"+currentUser);
        return Result.success(reportService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }
    //    ????????????
    @PostMapping
    public Result save(@RequestBody Report report){
        reportService.saveReport(report);
        flushRedis(Constants.REPORT_KEY);
        return Result.success();
    }
    //??????
    @GetMapping
    public Result findAll(){
        String jsonStr = stringRedisTemplate.opsForValue().get(Constants.REPORT_KEY);
        List<Report> reports;
        if(StrUtil.isBlank(jsonStr)){
            reports = reportService.list();
            stringRedisTemplate.opsForValue().set(Constants.REPORT_KEY,JSONUtil.toJsonStr(reports));
        }else{
            reports = JSONUtil.toBean(jsonStr, new TypeReference<List<Report>>() {
            }, true);
        }
        return Result.success(reports) ;
    }
    //??????
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        reportService.removeById(id);
        flushRedis(Constants.REPORT_KEY);
        return Result.success();
    }
    //????????????
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        return Result.success(reportService.removeByIds(ids));
    }
    /**
     * ????????????
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception{
        //????????????????????????????????????
        List<Report> list=reportService.list();
        //???????????????,???????????????
        ExcelWriter writer= ExcelUtil.getWriter(true);
        //???????????????????????????
        writer.addHeaderAlias("address","??????????????????");
        writer.addHeaderAlias("territory","????????????");
        writer.addHeaderAlias("classification","????????????????????????");
        writer.addHeaderAlias("enterpriseName","??????????????????");
        writer.addHeaderAlias("createTime","????????????");
        writer.addHeaderAlias("phone","?????????");
        writer.addHeaderAlias("contactPerson","???????????????");
        writer.addHeaderAlias("detail","????????????");
        writer.addHeaderAlias("proposal","????????????");
        writer.addHeaderAlias("term","????????????");
        writer.addHeaderAlias("updateTime","??????????????????");
        writer.addHeaderAlias("createTime","????????????");
        writer.addHeaderAlias("result","????????????");
        writer.addHeaderAlias("measures","??????????????????");


        //???????????????list ?????????excel,??????????????????,??????????????????
        writer.write(list,true);
        //??????????????????????????????
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName= URLEncoder.encode("????????????","utf-8");
        response.setHeader("Content-Disposition","attachment;filename="+fileName+".xlsx");

        ServletOutputStream out=response.getOutputStream();
        writer.flush(out, true);
        writer.close();
        IoUtil.close(out);
    }
    /**
     * ????????????
     */
    @PostMapping("/import")
    public Result imp(MultipartFile file) throws Exception{
        InputStream inputStream=file.getInputStream();
        ExcelReader reader=ExcelUtil.getReader(inputStream);
        //?????? javaBean ???????????????Excel ?????????
//        List<User> list=reader.readAll(User.class);

        //??????????????????
        List<List<Object>> list = reader.read(1);
        List<Report> reports = CollUtil.newArrayList();
        for(List<Object> row:list){
            Report report=new Report();
            report.setAddress(row.get(0).toString());
            report.setProposal(row.get(1).toString());
            report.setClassification(row.get(2).toString());
            report.setTerritory(row.get(3).toString());
            report.setPhone(row.get(4).toString());
            report.setContactPerson(row.get(5).toString());
            report.setResult(row.get(6).toString());
            report.setTerm(row.get(7).toString());
            report.setMeasures(row.get(8).toString());
            report.setDetail(row.get(9).toString());
            report.setEnterpriseName(row.get(10).toString());
            reports.add(report);
        }
        reportService.saveBatch(reports);
        return Result.success(true);
    }
    //    ????????????
    @PostMapping("/update")
    public Result update(@RequestBody Report report){
        reportService.updateById(report);
        flushRedis(Constants.REPORT_KEY);
        return Result.success();
    }

    private void flushRedis(String key){
        stringRedisTemplate.delete(key);
    }
}

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
//        System.out.println("获取当前用户信息："+currentUser);
        return Result.success(reportService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }
    //    新增修改
    @PostMapping
    public Result save(@RequestBody Report report){
        reportService.saveReport(report);
        flushRedis(Constants.REPORT_KEY);
        return Result.success();
    }
    //查询
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
    //删除
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        reportService.removeById(id);
        flushRedis(Constants.REPORT_KEY);
        return Result.success();
    }
    //批量删除
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        return Result.success(reportService.removeByIds(ids));
    }
    /**
     * 导出接口
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception{
        //从数据库中查询所有的数据
        List<Report> list=reportService.list();
        //在内存操作,写到浏览器
        ExcelWriter writer= ExcelUtil.getWriter(true);
        //自定义表头标题别名
        writer.addHeaderAlias("address","隐患具体地址");
        writer.addHeaderAlias("territory","负责区域");
        writer.addHeaderAlias("classification","隐患企业行业分类");
        writer.addHeaderAlias("enterpriseName","隐患企业名称");
        writer.addHeaderAlias("createTime","上报时间");
        writer.addHeaderAlias("phone","手机号");
        writer.addHeaderAlias("contactPerson","负责人姓名");
        writer.addHeaderAlias("detail","隐患详情");
        writer.addHeaderAlias("proposal","整改建议");
        writer.addHeaderAlias("term","整改期限");
        writer.addHeaderAlias("updateTime","最近反馈时间");
        writer.addHeaderAlias("createTime","创建时间");
        writer.addHeaderAlias("result","反馈结果");
        writer.addHeaderAlias("measures","最近整改措施");


        //一次性写出list 对象到excel,使用默认样式,强制输出标题
        writer.write(list,true);
        //设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName= URLEncoder.encode("隐患信息","utf-8");
        response.setHeader("Content-Disposition","attachment;filename="+fileName+".xlsx");

        ServletOutputStream out=response.getOutputStream();
        writer.flush(out, true);
        writer.close();
        IoUtil.close(out);
    }
    /**
     * 导入接口
     */
    @PostMapping("/import")
    public Result imp(MultipartFile file) throws Exception{
        InputStream inputStream=file.getInputStream();
        ExcelReader reader=ExcelUtil.getReader(inputStream);
        //通过 javaBean 的方式读取Excel 内对象
//        List<User> list=reader.readAll(User.class);

        //忽略表头中文
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
    //    新增修改
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

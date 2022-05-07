package com.example.demo.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Constants;
import com.example.demo.common.Result;
import com.example.demo.entity.Files;
import com.example.demo.exception.ServiceException;
import com.example.demo.mapper.FileMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * 文件上传相关接口
 */
@RestController
@RequestMapping("/file")
public class FileController {
    @Value("${files.upload.path}")
    private String fileUploadPath;

    @Resource
    FileMapper fileMapper;
    /**
     * 文件上传接口
     * @param file 前端传过来的文件
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);
        long size = file.getSize();

        //定义一个文件唯一表识码
        String uuid = IdUtil.fastSimpleUUID();
        String fileUUID=uuid+ StrUtil.DOT+type;

        File uploadFile = new File(fileUploadPath+fileUUID);
        //先存储到磁盘中
        File parentFile = uploadFile.getParentFile();
        //判断文件是否存在,若不存在则创建一个新的文件目录
        if(!parentFile.exists()){
            parentFile.mkdirs();
        }

            //获取文件url
            String url;
            //获取道德文件存储到磁盘目录中
            file.transferTo(uploadFile);
            String md5= SecureUtil.md5(uploadFile);
            //从数据库中查询相同md5
            Files dbFiles = getFileByMd5(md5);
            if(dbFiles!=null){
                url=dbFiles.getUrl();
                uploadFile.delete();
            }else{
                url="http://localhost:8088/file/"+fileUUID;
            }

        //再存储到数据库中
        Files saveFile = new Files();
        saveFile.setName(originalFilename);
        saveFile.setSize(size/1024);
        saveFile.setType(type);
        saveFile.setUrl(url);
        saveFile.setMd5(md5);
        fileMapper.insert(saveFile);
        return url;
    }

    /**
     * 文件下载接口  http://localhost:8088/file/{fileUUID}
     * @param fileUUID
     * @param response
     * @throws IOException
     */
    @GetMapping("/{fileUUID}")
    public void download(@PathVariable String fileUUID, HttpServletResponse response) throws IOException {
        //根据文件唯一标示码获取文件
        File uploadFile = new File(fileUploadPath +fileUUID);
        ServletOutputStream os = response.getOutputStream();
        response.addHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode(fileUUID,"UTF-8"));
        response.setContentType("/application/octet-stream");
        //读取文件字节流
        os.write(FileUtil.readBytes(uploadFile));
        os.flush();
        os.close();

    }

    //获取文件的MD5
    private Files getFileByMd5(String md5){
        //查询md5是否存在
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5",md5);
        List<Files> filesList = fileMapper.selectList(queryWrapper);
        return filesList.size()==0?null:filesList.get(0);
    }
    //    新增修改
    @PostMapping("/update")
    public Result update(@RequestBody Files files){
        return Result.success(fileMapper.updateById(files));
    }
    //删除
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        Files files = fileMapper.selectById(id);
        files.setIsDelete(true);
        fileMapper.updateById(files);
        return Result.success();
    }
    //根据id查询
    @GetMapping("/detail/{id}")
    public Result getById(@PathVariable Integer id){
        return Result.success(fileMapper.selectById(id));
    }
    //批量删除
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids)  {
        try {
            QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("id",ids);
            List<Files> files = fileMapper.selectList(queryWrapper);
            for(Files file :files){
                file.setIsDelete(true);
                fileMapper.updateById(file);
            }
            return Result.success();
        }catch (Exception e){
            throw new ServiceException(Constants.code_400,"参数错误");
        }

    }
    /**
     * 分页查询接口
     * @param pageNum
     * @param pageSize
     * @param name
     * @return
     */
    //mybatis-plus 方式
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String name){
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_delete",false);
        queryWrapper.orderByDesc("id");
        if(!"".equals(name)){
            queryWrapper.like("name",name);
        }
        return Result.success(fileMapper.selectPage(new Page<>(pageNum,pageSize),queryWrapper)) ;
    }
}

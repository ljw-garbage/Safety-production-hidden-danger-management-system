package com.example.demo.controller.dto;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Constants;
import com.example.demo.common.Result;
import com.example.demo.entity.Detail;
import com.example.demo.service.DetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/details")
public class DetailController {
    @Resource
    DetailService detailService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //    新增修改
    @PostMapping
    public Result save(@RequestBody Detail detail){
        detailService.saveDetail(detail);
        flushRedis(Constants.DETAILS_KEY);
        return Result.success();
    }
    /**
     *  查询所有
     */
    @GetMapping
    public Result findAll(){
        try{
            //1.从缓存获取数据
            String jsonStr = stringRedisTemplate.opsForValue().get(Constants.DETAILS_KEY);
            List<Detail> details;
            if(StrUtil.isBlank(jsonStr)){ //2.取出来的json 为空时
                details = detailService.list(); //3.从数据库中取出数据
                //4.再缓存到redis
                //将details对象 再转成 json 字符串
                stringRedisTemplate.opsForValue().set(Constants.DETAILS_KEY,JSONUtil.toJsonStr(details));
            }else{
                //5.如果有,从redis 缓存中获取数据
                details = JSONUtil.toBean(jsonStr, new TypeReference<List<Detail>>() {
                }, true);
            }
            return Result.success(details);
        }catch (Exception e){
            return Result.error(Constants.code_500,"缓存加载失败");
        }

    }
    /**
     * 根据id 查询详情
     */
    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id){
        QueryWrapper<Detail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        return Result.success(detailService.getOne(queryWrapper));
    }
    //删除
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        detailService.removeById(id);
        flushRedis(Constants.DETAILS_KEY);
        return Result.success();
    }

    //批量删除
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        return Result.success(detailService.removeByIds(ids));
    }
    /**
     * 分页查询
     */
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam String title
    ) {
        QueryWrapper<Detail> queryWrapper = new QueryWrapper<>();
//        queryWrapper.orderByDesc("id");
        if(!"".equals(title)){
            queryWrapper.like("title",title);
        }

        return Result.success(detailService.page(new Page<>(pageNum, pageSize),queryWrapper));
    }
    //更新
    @PostMapping("/update")
    public Result update(@RequestBody Detail detail){
        detailService.updateById(detail);
        flushRedis(Constants.DETAILS_KEY);
        return Result.success();
    }

    private void flushRedis(String key){
        stringRedisTemplate.delete(key);
    }
}


package com.example.demo.controller.dto;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.example.demo.common.Constants;
import com.example.demo.common.Result;
import com.example.demo.dto.ImageDto;
import com.example.demo.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/images")
public class ImageController {
    @Resource
    ImageService imageService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /**
     *  查询所有
     */
    @GetMapping
    public Result findAll(){
        try{
            String jsonStr = stringRedisTemplate.opsForValue().get(Constants.IMAGE_KEY);
            List<ImageDto> imageDtos;
            if(StrUtil.isBlank(jsonStr)){
                imageDtos = imageService.list();
                stringRedisTemplate.opsForValue().set(Constants.IMAGE_KEY,JSONUtil.toJsonStr(imageDtos));
            }else{

                imageDtos=JSONUtil.toBean(jsonStr, new TypeReference<List<ImageDto>>() {
                },true);
            }
            return Result.success(imageDtos);
        }catch (Exception e){
           return Result.error(Constants.code_500,"缓存加载失败");
        }

    }
//    public Result findAll(){
//        return Result.success(imageService.list()) ;
//    }
}

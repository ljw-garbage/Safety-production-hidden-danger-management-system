package com.example.demo.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class RedisClient {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /*
     * @ClassName RedisClient
     * @Desc TODO   设置缓存（没有时间限制）
     * @Date 2021-07-24 16:11
     * @Version 1.0
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /*
     * @ClassName RedisClient
     * @Desc TODO   设置缓存（有时间限制，单位为 秒）
     * @Date 2021-07-24 16:11
     * @Version 1.0
     */
    public void set(String key, Object value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    /*
     * @ClassName RedisClient
     * @Desc TODO   删除缓存，并返回是否删除成功
     * @Date 2021-07-24 16:11
     * @Version 1.0
     */
    public boolean delete(String key) {
        redisTemplate.delete(key);
        // 如果还存在这个 key 就证明删除失败
        if (redisTemplate.hasKey(key)) {
            return false;
            // 不存在就证明删除成功
        } else {
            return true;
        }
    }

    /*
     * @ClassName RedisClient
     * @Desc TODO   取出缓存
     * @Date 2021-07-24 16:12
     * @Version 1.0
     */
    public Object get(String key) {
        if (redisTemplate.hasKey(key)) {
            return redisTemplate.opsForValue().get(key);
        } else {
            return null;
        }
    }

    /*
     * @ClassName RedisClient
     * @Desc TODO   获取失效时间（-2：失效 / -1：没有时间限制）
     * @Date 2021-07-24 16:15
     * @Version 1.0
     */
    public long getExpire(String key) {
        // 判断是否存在
        if (redisTemplate.hasKey(key)) {
            return redisTemplate.getExpire(key);
        } else {
            return Long.parseLong(-2 + "");
        }

    }
}
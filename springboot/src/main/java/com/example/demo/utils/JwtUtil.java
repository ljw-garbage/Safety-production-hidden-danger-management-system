////package com.example.demo.utils;
////
////import com.auth0.jwt.JWT;
////import com.auth0.jwt.JWTCreator;
////import com.auth0.jwt.algorithms.Algorithm;
////import org.springframework.stereotype.Component;
////
////
////import java.util.Calendar;
////import java.util.Map;
////@Component
////public class JwtUtil {
////    private static final String SING = "123456";
////
////    public static String  getToken(Map<String, String> map){
////        Calendar instance = Calendar.getInstance();
////        //默认7天
////        instance.add(Calendar.DATE,7);
////
////        //创建Jwt builder
////        JWTCreator.Builder builder = JWT.create();
////
////        map.forEach((k,v)->{
////            builder.withClaim(k,v);
////        });
////
////        String token = builder.withExpiresAt(instance.getTime())
////                .sign(Algorithm.HMAC256(SING));
////        return token;
////    }
////
////    /**
////     * 验证token合法性
////     * @param token
////     */
////    public static void verify(String token){
////        JWT.require(Algorithm.HMAC256(SING)).build().verify(token);
////    }
////
//////    public static DecodedJWT getTokenInfo(){ }
////
//
//package com.example.demo.utils;
//
//import io.jsonwebtoken.*;
//
//import javax.crypto.SecretKey;
//import javax.crypto.spec.SecretKeySpec;
//import javax.security.auth.Subject;
//import java.util.Base64;
//import java.util.Date;
//import java.util.UUID;
//
///**
// * JWT 工具类
// */
//public class JwtUtil{
//    //有效期
//    public static final Long JWT_TTL=60*60*1000L;
//    //设置密钥明文
//    public static final String JWT_KEY="123456";
//
//    public static String getUUID(){
//        String token= UUID.randomUUID().toString().replaceAll("-","");
//        return token;
//    }
//    /**
//     * 生成jwt
//     */
//    public static String createJWTBuilder(String subject,Long ttlMillis,String uuid){
//        JwtBuilder builder=getJwtBuilder(subject,ttlMillis,getUUID());
//        return builder.compact();
//    }
//    private static JwtBuilder getJwtBuilder(String subject,Long ttlMillis,String uuid){
//        SignatureAlgorithm signatureAlgorithm=SignatureAlgorithm.HS256;
//        SecretKey secretKey=generalKey();
//        long nowMillis=System.currentTimeMillis();
//        Date now=new Date(nowMillis);
//        if(ttlMillis==null){
//            ttlMillis=JwtUtil.JWT_TTL;
//        }
//        long expMills=nowMillis+ttlMillis;
//        Date expDate=new Date(expMills);
//        return Jwts.builder()
//                .setId(uuid)   //唯一的Id
//                .setSubject(subject) // 主题  可以是JSON 数据
//                .setIssuer("ljw")
//                .setIssuedAt(now)
//                .signWith(signatureAlgorithm,secretKey)
//                .setExpiration(expDate);
//    }
//    /**
//     * 创建token
//     * @param id
//     * @return
//     */
//    public static String createJWT(String id,long ttlMillis,String subject) {
//        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, id);// 设置过期时间
//        return builder.compact();
//    }
//    /**
//     * 生成jtw
//     * @param subject token中要存放的数据（json格式）
//     * @return
//     */
//    public static String createJWT(String subject) {
//        JwtBuilder builder = getJwtBuilder(subject, null, getUUID());// 设置过期时间
//        return builder.compact();
//    }
//    /**
//     * 生成jtw
//     * @param subject token中要存放的数据（json格式）
//     * @param ttlMillis token超时时间
//     * @return
//     */
//    public static String createJWT(String subject, Long ttlMillis) {
//        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, getUUID());// 设置过期时间
//        return builder.compact();
//    }
//    public static void main(String[] args) throws Exception{
//        String token="eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJjYWM2ZDVhZi1mNjVlLTQ0MDAtYjcxMi0zYWEwOGIyOTIwYjQiLCJzdWIiOiJzZyIsImlzcyI6InNnIiwiaWF0IjoxNjM4MTA2NzEyLCJleHAiOjE2MzgxMTAzMTJ9.JVsSbkP94wuczb4QryQbAke3ysBDIL5ou8fWsbt_ebg";
//        Claims claims=parseJWT(token);
//        System.out.println(claims);
//    }
//    /**
//     * 生成加密后的密钥secretKey
//     * @return
//     */
//    public static SecretKey generalKey(){
//        byte[] encodedKey= Base64.getDecoder().decode(JwtUtil.JWT_KEY);
//        SecretKey key=new SecretKeySpec(encodedKey,0,encodedKey.length,"AES");
//        return key;
//    }
//    /**
//     * 解析
//     */
//    public static Claims parseJWT(String jwt)throws Exception{
//        SecretKey secretKey=generalKey();
//        return Jwts.parser()
//                .setSigningKey(secretKey)
//                .parseClaimsJws(jwt)
//                .getBody();
//    }
//}
package com.example.demo.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.entity.User;
import com.example.demo.service.LoginService;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
@Component
public class JwtUtil {

    private static LoginService staticLoginFormService;
    @Resource
    private LoginService loginService;
    @PostConstruct
    public void setUserService(){
        staticLoginFormService=loginService;
    }
    /**
     *
     *生成tokenId
     */
    public static String genToken(String userId,String sign){
        return JWT.create().withAudience(userId) // 将 user id 保存到 token 里面
                .withExpiresAt(DateUtil.offsetHour(new Date(),2)) //2小时后token过期
                .sign(Algorithm.HMAC256(sign)); // 以 password 作为 token 的密钥
    }
    /**
     * 获取当前用户信息
     */
    public static User getCurrentUser(){
        try{
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token=request.getHeader("token");
            if(StrUtil.isNotBlank(token)){
                String userId=JWT.decode(token).getAudience().get(0);
                return staticLoginFormService.getById(Integer.valueOf(userId));
            }
        }catch (Exception e){
            return null;
        }
        return null;
    }
}

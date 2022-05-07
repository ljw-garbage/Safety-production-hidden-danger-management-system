package com.example.demo.controller.dto;

import com.example.demo.common.Constants;
import com.example.demo.common.Result;
import com.example.demo.exception.ServiceException;
import com.example.demo.utils.RandomValidateCode;
import com.example.demo.utils.VerifyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/code")
public class VerifyCodeController {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping
    //返回验证码图片
    public void getCaptchaImg(HttpServletRequest request, HttpServletResponse response, HttpSession session) {

        try {

            response.setContentType("image/png");
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Expire", "0");
            response.setHeader("Pragma", "no-cache");
            VerifyUtil validateCode = new VerifyUtil();
            // getRandomCodeImage方法会直接将生成的验证码图片写入response
            validateCode.getRandomCodeImage(request, response);
             System.out.println("session里面存储的验证码为："+session.getAttribute("CODE"));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 生成验证码,返回的是 base64
    @GetMapping("/getCaptchaBase64")
    public Object getCaptchaBase64(HttpServletRequest request, HttpServletResponse response,HttpSession session) {

        Map result = new HashMap();
        try {
            response.setContentType("image/png");
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Expire", "0");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Set-Cookie", "name=value; HttpOnly");
            VerifyUtil validateCode = new VerifyUtil();
            // 返回base64
            String base64String = validateCode.getRandomCodeBase64(request, response);
            result.put("responseBody", "data:image/png;base64," + base64String);
            result.put("msg", "success");
            //http://tool.chinaz.com/tools/imgtobase/  base64直接转为图片网站
            System.out.println("结果：" + result.get("responseBody"));

            System.out.println("session64里面存储的验证码为："+session.getAttribute("CODE"));
        } catch (Exception e) {
            System.out.println(e);
        }

        return result;
    }
    //验证码校验

    @GetMapping("/checkCaptcha")
    public Result getCheckCaptcha(@RequestParam("code") String code, HttpSession session) {
//        String Code=null;
        try {
            //toLowerCase() 不区分大小写进行验证码校验
//            System.out.println("session里面存储的验证码为："+session.getAttribute("CODE"));
            String sessionCode= String.valueOf(session.getAttribute("CODE")).toLowerCase();
            System.out.println("session里的验证码："+sessionCode);
            String receivedCode=code.toLowerCase();
            System.out.println("用户的验证码："+receivedCode);
//            return !sessionCode.equals("") && !receivedCode.equals("") && sessionCode.equals(receivedCode);
            if(!sessionCode.equals("") && !receivedCode.equals("") && sessionCode.equals(receivedCode)){
                return Result.success("校验成功");
            }else{
                throw new ServiceException(Constants.code_400,"验证码错误");
            }

        } catch (Exception e) {
//            return false;
            return Result.error(Constants.code_400,"验证码校验失败");
        }

    }
    /**
     * 生成验证码
     */

    @GetMapping(value = "/getVerify")
    public void getVerify(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("image/png");//设置相应类型,告诉浏览器输出的内容为图片
            response.setHeader("Pragma", "No-cache");//设置响应头信息，告诉浏览器不要缓存此内容
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expire", 0);
            RandomValidateCode randomValidateCode = new RandomValidateCode();
            randomValidateCode.getRandCode(request, response);//输出验证码图片方法
            String id = UUID.randomUUID().toString();
            response.setHeader("id",id);
//            stringRedisTemplate.opsForValue().set(Constants.CAPTCHA_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 校验验证码
     */

    @PostMapping(value = "/checkVerify")
    public Result checkVerify(@RequestBody Map<String, Object> requestMap, HttpSession session) {
        try{
            //从session中获取随机数
            String inputStr = requestMap.get("inputStr").toString();
            String random = String.valueOf(session.getAttribute("CODE")).toLowerCase() ;
            System.out.println("session里的验证码："+ random);
            if (!random.equals("")&&random.equals(inputStr.toLowerCase())) {
                return Result.success("校验成功");
            } else {
                throw new ServiceException(Constants.code_400,"验证码错误");
            }
        }catch (Exception e){
            return Result.error(Constants.code_400,"验证码错误");
        }
    }
}
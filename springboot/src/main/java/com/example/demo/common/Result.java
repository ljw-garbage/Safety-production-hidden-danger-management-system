package com.example.demo.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private String code;
    private String msg;
    private Object data;
//    private String token;


    public static Result success(){
        return new Result(Constants.code_200,"成功",null);
    }
    public static Result success(Object data){
        return new Result(Constants.code_200,"成功",data);
    }
    public static Result error(String code,String msg){
        return new Result(code,msg,null);
    }
    public static Result error(){
        return new Result(Constants.code_500,"系统错误",null);
    }
}

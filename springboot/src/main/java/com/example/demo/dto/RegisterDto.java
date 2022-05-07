package com.example.demo.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RegisterDto {

    private String account;
    private String username;
    private String password;
    private String secondPassword;
}


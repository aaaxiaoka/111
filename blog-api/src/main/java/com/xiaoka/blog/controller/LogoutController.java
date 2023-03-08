package com.xiaoka.blog.controller;

import com.xiaoka.blog.Dao.vo.Result;
import com.xiaoka.blog.Dao.vo.params.LoginParam;
import com.xiaoka.blog.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("logout")
public class LogoutController {

    @Autowired
    private LoginService loginService;

    @GetMapping
    public Result login(@RequestHeader("Authorization") String token){
        //登录 验证用户 访问表
        return loginService.logout(token);
    };
}

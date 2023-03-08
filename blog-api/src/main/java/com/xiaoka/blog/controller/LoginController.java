package com.xiaoka.blog.controller;

import com.xiaoka.blog.Dao.vo.Result;
import com.xiaoka.blog.Dao.vo.params.LoginParam;
import com.xiaoka.blog.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping
    public Result login(@RequestBody LoginParam loginParam){
        //登录 验证用户 访问表
        return loginService.login(loginParam);
    };
}

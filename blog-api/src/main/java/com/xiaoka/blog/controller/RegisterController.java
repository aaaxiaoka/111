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
@RequestMapping("register")
public class RegisterController {

    @Autowired
    private LoginService loginService;

    @PostMapping
    public Result register(@RequestBody LoginParam loginParam){
        //sso单点登录，如果后期登录注册功能分开（可以独立提供接口服务）
        return loginService.register(loginParam);
    }
}

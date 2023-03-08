package com.xiaoka.blog.controller;


import com.xiaoka.blog.Dao.pojo.SysUser;
import com.xiaoka.blog.Dao.vo.Result;
import com.xiaoka.blog.utils.UserThreadLocal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {
    //sysUser
    @RequestMapping
    public Result test(){
        SysUser sysUser = UserThreadLocal.get();
        System.out.println(sysUser);
        return Result.success(null);
    }
}
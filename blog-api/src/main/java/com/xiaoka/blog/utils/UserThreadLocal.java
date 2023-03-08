package com.xiaoka.blog.utils;

import com.xiaoka.blog.Dao.pojo.SysUser;

public class UserThreadLocal {
    private UserThreadLocal(){};
    //线程变量隔离

    private static final ThreadLocal<SysUser> LOCAL = new ThreadLocal<>();

    public  static void put(SysUser sysUser){
        LOCAL.set(sysUser);
    }
    public static SysUser get(){
        return LOCAL.get();
    }
    public static void remove(){
        LOCAL.remove();
    }
}

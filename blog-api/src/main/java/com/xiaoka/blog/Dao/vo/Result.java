package com.xiaoka.blog.Dao.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Result {
    private  boolean success;
    private int code;
    private String mse;
    private Object data;

    public  static Result success(Object data){
        return new Result(true,200,"success",data);
    }

    public static Result fail(int code,String mse){
        return new Result(false,code,mse,null);
    }
}

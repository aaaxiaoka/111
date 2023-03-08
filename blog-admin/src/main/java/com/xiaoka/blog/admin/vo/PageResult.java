package com.xiaoka.blog.admin.vo;

import lombok.Data;
import org.apache.catalina.LifecycleState;

import java.util.List;

@Data
public class PageResult<T> {
    private List<T> list;

    private Long total;
}

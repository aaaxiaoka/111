package com.xiaoka.blog.service;


import com.xiaoka.blog.Dao.vo.CategoryVo;
import com.xiaoka.blog.Dao.vo.Result;


public interface CategoryService {

    CategoryVo findCategoryById(Long id);

    Result findAll();

    Result findAllDetails();

    Result categoriesDetailById(Long id);
}
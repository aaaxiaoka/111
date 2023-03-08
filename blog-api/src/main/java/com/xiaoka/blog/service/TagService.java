package com.xiaoka.blog.service;

import com.xiaoka.blog.Dao.pojo.Tag;
import com.xiaoka.blog.Dao.vo.Result;
import com.xiaoka.blog.Dao.vo.TagVo;

import java.util.List;

public interface TagService {
    List<TagVo> findTagsByArticleId(Long articleId);

    Result hots(int limit);

    Result findAll();

    Result findAllDetail();

    Result findDetailById(Long id);
}

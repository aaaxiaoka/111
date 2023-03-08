package com.xiaoka.blog.Dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoka.blog.Dao.dos.Archives;
import com.xiaoka.blog.Dao.pojo.Article;

import java.util.List;

public interface ArticleMapper extends BaseMapper<Article> {
    List<Archives> listArchives();

    IPage<Article> listArticle(
            Page<Article> page,
            Long categoryId,
            Long tagId,
            String year,
            String month);
}

package com.xiaoka.blog.service;

import com.xiaoka.blog.Dao.vo.ArticleVo;
import com.xiaoka.blog.Dao.vo.Result;
import com.xiaoka.blog.Dao.vo.params.ArticleParam;
import com.xiaoka.blog.Dao.vo.params.PageParams;

public interface ArticleService {

    /**
     * 分页查询 文章列表
     * @param pageParams
     * @return
     */
    Result listArticle(PageParams pageParams);


    /**
     * 最热文章
     * @param limit
     * @return
     */
    Result hotArticle(int limit);

    /**
     * 最新文章
     * @param limit
     * @return
     */
    Result newArticles(int limit);


    /**
     * 文章归档
     * @return
     */
    Result listArchives();

    /**
     * 查询文章
     * @param id
     * @return
     */
    ArticleVo findArticleById(Long id);

    /**
     * 发布文章
     * @param articleParam
     * @return
     */
    Result publish(ArticleParam articleParam);
}

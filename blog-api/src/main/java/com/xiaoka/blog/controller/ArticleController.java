package com.xiaoka.blog.controller;


import com.xiaoka.blog.Dao.vo.ArticleVo;
import com.xiaoka.blog.Dao.vo.Result;
import com.xiaoka.blog.Dao.vo.params.ArticleParam;
import com.xiaoka.blog.Dao.vo.params.PageParams;
import com.xiaoka.blog.common.aop.LogAnnotation;
import com.xiaoka.blog.common.cache.Cache;
import com.xiaoka.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//json数据交互
@RestController

@RequestMapping("articles")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    /**
     * 首页 文章列表
     * @param pageParams
     * @return
     */
    @PostMapping
    //加上此注解，代表要对此接口进行日志记录
//    @LogAnnotation(module="文章",operator="获取文章列表")
    public Result listArticles(@RequestBody PageParams pageParams){

        return articleService.listArticle(pageParams);

    }

    /**
     * 首页最热文章
     * @return
     */
    @PostMapping("hot")
    @LogAnnotation(module="最热文章",operator="获取最热文章")
    @Cache(expire = 5 * 60 * 1000,name = "hot_article")
    public Result hotArticle(){

        //int i = 10/0;
        int limit = 5;
        return articleService.hotArticle(limit);

    }

    /**
     * 首页最新文章
     * @return
     */
    @PostMapping("new")
    public Result newArticles(){

        int limit = 5;
        return articleService.newArticles(limit);

    }

    /**
     * 首页文章归档
     * @return
     */
    @PostMapping("listArchives")
    public Result listArchives(){


        return articleService.listArchives();

    }

    /**
     * 查看文章
     */
    @PostMapping("view/{id}")
    public Result findArticleById(@PathVariable("id") Long id){
        ArticleVo articleVo = articleService.findArticleById(id);
        return Result.success(articleVo);
    }

    @PostMapping("publish")
    public Result publish(@RequestBody ArticleParam articleParam){
        return articleService.publish(articleParam);
    }
}

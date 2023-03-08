package com.xiaoka.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiaoka.blog.Dao.mapper.ArticleMapper;
import com.xiaoka.blog.Dao.pojo.Article;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ThreadService {

    @Async("taskExecutor")
    public void updateViewCount(ArticleMapper articleMapper, Article article) {

        int viewCount = article.getViewCounts();
        Article articleUpdate = new Article();
        //设置浏览数
        articleUpdate.setViewCounts(viewCount+1);
        //spring更新数据的操作
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getId,article.getId());
        //保证线程安全
        queryWrapper.eq(Article::getViewCounts,viewCount);
        articleMapper.update(articleUpdate,queryWrapper);

        try {
            Thread.sleep(5000);
            System.out.println("更新完成！");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

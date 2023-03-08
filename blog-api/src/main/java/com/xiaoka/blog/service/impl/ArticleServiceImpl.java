package com.xiaoka.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoka.blog.Dao.dos.Archives;
import com.xiaoka.blog.Dao.mapper.ArticleBodyMapper;
import com.xiaoka.blog.Dao.mapper.ArticleMapper;
import com.xiaoka.blog.Dao.mapper.ArticleTagMapper;
import com.xiaoka.blog.Dao.pojo.Article;
import com.xiaoka.blog.Dao.pojo.ArticleBody;
import com.xiaoka.blog.Dao.pojo.ArticleTag;
import com.xiaoka.blog.Dao.pojo.SysUser;
import com.xiaoka.blog.Dao.vo.ArticleBodyVo;
import com.xiaoka.blog.Dao.vo.ArticleVo;
import com.xiaoka.blog.Dao.vo.Result;
import com.xiaoka.blog.Dao.vo.TagVo;
import com.xiaoka.blog.Dao.vo.params.ArticleParam;
import com.xiaoka.blog.Dao.vo.params.PageParams;
import com.xiaoka.blog.service.*;
import com.xiaoka.blog.utils.UserThreadLocal;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private TagService tagService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ArticleTagMapper articleTagMapper;

    private List<ArticleVo> copyList(List<Article> records,boolean isTag ,boolean isAuthor) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record,isTag,isAuthor));

        }
        return articleVoList;
    }

    private ArticleVo copy(Article article,boolean isTag ,boolean isAuthor){
        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(String.valueOf(article.getId()));
        BeanUtils.copyProperties(article,articleVo);
        //createDate 在数据库是long类型

        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        //并不是所有接口，都需要标签,作者信息
        if(isTag){
            Long articleId = article.getId();
            articleVo.setTags(tagService.findTagsByArticleId(articleId));
        }
        if(isAuthor){
            long authorId = article.getAuthorId();
            articleVo.setAuthor(sysUserService.findUserById(authorId).getNickname());
        }
        return  articleVo;
    }
    //采用动态sql语句查询文章
    @Override
    public Result listArticle(PageParams pageParams){
        Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPageSize());
        IPage<Article> articleIPage = this.articleMapper.listArticle(
                page,
                pageParams.getCategoryId(),
                pageParams.getTagId(),
                pageParams.getYear(),
                pageParams.getMonth());
        return Result.success(copyList(articleIPage.getRecords(),true,true));

    }


//    @Override
//    public Result listArticle(PageParams pageParams) {
//        /**
//         * 分页查询
//         */
//        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
//
//        //排序
//        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
//        //根据categoryId筛选出文章
//        if(pageParams.getCategoryId() != null){
//            queryWrapper.eq(Article::getCategoryId,pageParams.getCategoryId());
//        }
//        //根据tagId筛选文章
//        List<Long> articleIdList = new ArrayList<>();
//        //需要在article_tag 表里查询到与 tagId一致的articleId
//        if(pageParams.getTagId() != null){
//            LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
//            articleTagLambdaQueryWrapper.eq(ArticleTag::getTagId,pageParams.getTagId());
//            List<ArticleTag> articleTags = articleTagMapper.selectList(articleTagLambdaQueryWrapper);
//            for (ArticleTag articleTag : articleTags) {
//                articleIdList.add(articleTag.getArticleId());
//            }
//            //articleId in (1,2,5)
//            if(articleIdList.size()>0){
//                queryWrapper.in(Article::getId,articleIdList);
//            }
//        }
//        //是否置顶排序
//        //queryWrapper.orderByDesc(Article::getWeight);
//        //order by create_date desc
//        queryWrapper.orderByDesc(Article::getWeight,Article::getCreateDate);
//        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
//        List<Article> records = articlePage.getRecords();
//        //不能直接返回，此时是数据库对应数据,需要返回对应页面上的数据
//
//        List<ArticleVo> articleVoList = copyList(records,true,true);
//        return Result.success(articleVoList);
//    }

    @Override
    public Result hotArticle(int limit) {
        //排序
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.orderByDesc(Article::getViewCounts);
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit "+limit);
        //select id,title from article order by view_counts desc limit 5
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articles,false,false));
        //return null;
    }

    @Override
    public Result newArticles(int limit) {
        //排序
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.orderByDesc(Article::getCreateDate);
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit "+limit);
        //select id,title from article order by view_counts desc limit 5
        List<Article> articles = articleMapper.selectList(queryWrapper);

        return Result.success(copyList(articles,false,false));

    }

    @Override
    public Result listArchives() {
        List<Archives> archivesList = articleMapper.listArchives();
        return Result.success(archivesList);
    }

    @Autowired
    private ThreadService threadService;

    @Override
    public ArticleVo findArticleById(Long id) {
        /**
         * 1.根据id查询 文章信息
         * 2.根据文章bodyId和categoryid 去做关联查询
         */
        Article article = articleMapper.selectById(id);

        ArticleVo articleVo = copy(article, true, true,true,true);
        //查看过文章了，新增阅读数
        //查看完文章之后，本应该直接返回数据了，这时候做了一个更新操作，更新时加锁，阻塞其他的读操作，性能较差
        //更新 增加了此次接口的 耗时 一旦更新出问题，不能影响查看文章的操作
        //线程池 可以把更新操作放到线程池中进行，和主线程就不相关了

        threadService.updateViewCount(articleMapper,article);

        return articleVo;
    }




    @Autowired
    private CategoryService categoryService;

    private ArticleVo copy(Article article,boolean isTag ,boolean isAuthor,boolean isBody,boolean isCategory){
        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(String.valueOf(article.getId()));
        BeanUtils.copyProperties(article,articleVo);
        //createDate 在数据库是long类型

        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        //并不是所有接口，都需要标签,作者信息
        if(isTag){
            Long articleId = article.getId();
            articleVo.setTags(tagService.findTagsByArticleId(articleId));
        }
        if(isAuthor){
            long authorId = article.getAuthorId();
            articleVo.setAuthor(sysUserService.findUserById(authorId).getNickname());
        }
        if(isBody){
            Long bodyId = article.getBodyId();
            articleVo.setBody(findArticleByBodyId(bodyId));
        }
        if(isCategory){
            Long categoryId = article.getCategoryId();
            articleVo.setCategory(categoryService.findCategoryById(categoryId));

        }
        return  articleVo;
    }

    @Autowired
    private ArticleBodyMapper articleBodyMapper;
    private ArticleBodyVo findArticleByBodyId(Long bodyId) {
        ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }

    @Override
    //事务回滚
    @Transactional
    public Result publish(ArticleParam articleParam) {
        /**
         * 1.发布文章
         * 2.作者id 当前登录用户（登录连接器）
         * 3.标签 要将标签加入到关联列表中
         * 4.内容存储 body id;
         * 5.更新article
         */
        //包装文章对象
        SysUser sysUser = UserThreadLocal.get();

        Article article = new Article();
        article.setAuthorId(sysUser.getId());
        article.setCreateDate(System.currentTimeMillis());
        article.setViewCounts(0);
        article.setCommentCounts(0);
        article.setSummary(articleParam.getSummary());
        article.setTitle(articleParam.getTitle());
        article.setCategoryId(Long.parseLong(articleParam.getCategory().getId()));
        article.setWeight(Article.Article_Common);
        article.setBodyId(-1L);
        this.articleMapper.insert(article);
        //插入文章后生成文章id,去关联标签和body列表

        //tags
        List<TagVo> tags = articleParam.getTags();
        if(tags != null){
            for (TagVo tag : tags) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(article.getId());
                articleTag.setTagId(Long.parseLong(tag.getId()));
                this.articleTagMapper.insert(articleTag);
            }
        }

        //ArticleBody
        ArticleBody articleBody = new ArticleBody();
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBody.setArticleId(article.getId());
        articleBodyMapper.insert(articleBody);

        //将得到的ArticleBody id 更新到article表中
        article.setBodyId(articleBody.getId());
        articleMapper.updateById(article);
        Map<String,String> map = new HashMap<>();
        map.put("id",article.getId().toString());
        return Result.success(map);
    }

}

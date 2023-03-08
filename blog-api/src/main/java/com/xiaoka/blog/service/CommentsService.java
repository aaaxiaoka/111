package com.xiaoka.blog.service;

import com.xiaoka.blog.Dao.vo.Result;
import com.xiaoka.blog.Dao.vo.params.CommentParam;
import org.springframework.stereotype.Service;


public interface CommentsService {
    /**
     * 根据文章id查询评论
     * @param id
     * @return
     */
    Result commentByArticleId(Long id);

    /**
     *添加评论
     * @param commentParam
     * @return
     */
    Result comment(CommentParam commentParam);
}

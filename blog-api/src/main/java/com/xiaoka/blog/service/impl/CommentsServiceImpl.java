package com.xiaoka.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiaoka.blog.Dao.mapper.CommentsMapper;
import com.xiaoka.blog.Dao.pojo.Comment;
import com.xiaoka.blog.Dao.pojo.SysUser;
import com.xiaoka.blog.Dao.vo.CommentVo;
import com.xiaoka.blog.Dao.vo.Result;
import com.xiaoka.blog.Dao.vo.UserVo;
import com.xiaoka.blog.Dao.vo.params.CommentParam;
import com.xiaoka.blog.service.CommentsService;
import com.xiaoka.blog.service.SysUserService;
import com.xiaoka.blog.utils.UserThreadLocal;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
//实现类关联Service
public class CommentsServiceImpl implements CommentsService {
    @Autowired
    private CommentsMapper commentMapper;

    @Autowired
    private SysUserService sysUserService;

    @Override
    public Result commentByArticleId(Long id) {
        /**
         * 1.根据文章id查询评论列表
         * 2.根据作者id查询作者信息
         * 3.判断 如果 level=1 查询有没有子评论
         * 4.如果有根据评论id 进行查询 （parent_id)
         */
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getArticleId,id);
        queryWrapper.eq(Comment::getLevel,1);
        List<Comment> commentList = commentMapper.selectList(queryWrapper);
        //将comment包装为commentVo
        return Result.success(copyList(commentList));
    }

    private List<CommentVo> copyList(List<Comment> commentList) {
        List<CommentVo> commentVoList = new ArrayList<>();
        for (Comment comment : commentList) {
                commentVoList.add(copy(comment));
        }
        return commentVoList;
    }

    private CommentVo copy(Comment comment) {
        //包装commentVo对象
        CommentVo commentVo = new CommentVo();
        commentVo.setId(String.valueOf(comment.getId()));
        BeanUtils.copyProperties(comment,commentVo);
        //时间格式化
        commentVo.setCreateDate(new DateTime(comment.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        //包装userVo对象
        Long authorId = comment.getAuthorId();
        UserVo userVo = sysUserService.findUserVoById(authorId);
        commentVo.setAuthor(userVo);
        //评论的子评论
        List<CommentVo> commentVoList = findCommentsByParentId(comment.getId());
        commentVo.setChildrens(commentVoList);
        if(comment.getLevel()>1){
            Long toUid = comment.getToUid();
            UserVo toUserVo = sysUserService.findUserVoById(toUid);
            commentVo.setToUser(toUserVo);
        }
        return commentVo;
    }

    private List<CommentVo> findCommentsByParentId(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getParentId,id);
        queryWrapper.eq(Comment::getLevel,2);
        List<Comment> comments = this.commentMapper.selectList(queryWrapper);
        return copyList(comments);
    }

    @Override
    public Result comment(CommentParam commentParam) {
        SysUser sysUser = UserThreadLocal.get();

        //封装一个comment加入数据库
        Comment comment = new Comment();
        comment.setArticleId(commentParam.getArticleId());
        comment.setAuthorId(sysUser.getId());
        comment.setContent(commentParam.getContent());
        comment.setCreateDate(System.currentTimeMillis());
        Long parent = commentParam.getParent();
        if (parent == null || parent == 0) {
            comment.setLevel(1);
        }else{
            comment.setLevel(2);
        }
        comment.setParentId(parent == null ? 0 : parent);
        Long toUserId = commentParam.getToUserId();
        comment.setToUid(toUserId == null ? 0 : toUserId);
        this.commentMapper.insert(comment);
        return Result.success(null);
    }
}

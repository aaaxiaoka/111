package com.xiaoka.blog.Dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoka.blog.Dao.pojo.Tag;

import java.util.List;

public interface TagMapper extends BaseMapper<Tag> {
    /**
     * 根据文章id查询标签列表
     * @param articleId
     * @return
     */
    List<Tag> findTagsByArticleId(Long articleId);

    /**
     * 返回最热标签,前n条
     * @return
     */
    List<Long> findHotsTagIds(int limit);

    List<Tag> findTagsByArticleIds(List<Long> tagIds);
}

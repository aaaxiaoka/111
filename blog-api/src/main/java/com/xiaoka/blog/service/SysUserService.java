package com.xiaoka.blog.service;

import com.xiaoka.blog.Dao.pojo.SysUser;
import com.xiaoka.blog.Dao.vo.Result;
import com.xiaoka.blog.Dao.vo.UserVo;

public interface SysUserService {

    SysUser findUserById(Long id);

    SysUser findUser(String account, String password);

    /**
     * 根据token查询用户信息
     * @return
     */
    Result findUserByToken(String token);

    /**
     * 根据账户查找
     * @param account
     * @return
     */
    SysUser findUserByAccount(String account);

    /**
     * 保存数据库
     * @param sysUser
     */
    void save(SysUser sysUser);

    /**
     * 告状userVo对象
     * @param authorId
     * @return
     */
    UserVo findUserVoById(Long authorId);
}

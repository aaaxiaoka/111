package com.xiaoka.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiaoka.blog.Dao.mapper.ArticleMapper;
import com.xiaoka.blog.Dao.mapper.SysUserMapper;
import com.xiaoka.blog.Dao.pojo.Article;
import com.xiaoka.blog.Dao.pojo.SysUser;
import com.xiaoka.blog.Dao.vo.ErrorCode;
import com.xiaoka.blog.Dao.vo.LoginUserVo;
import com.xiaoka.blog.Dao.vo.Result;
import com.xiaoka.blog.Dao.vo.UserVo;
import com.xiaoka.blog.service.LoginService;
import com.xiaoka.blog.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private LoginService loginService;

    @Override
    public SysUser findUserById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null){
            sysUser = new SysUser();
            sysUser.setNickname("xiaoka");
        }

        return sysUser;
    }

    @Override
    public SysUser findUser(String account, String password) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount,account);
        queryWrapper.eq(SysUser::getPassword,password);
        queryWrapper.select(SysUser::getAccount,SysUser::getId,SysUser::getAvatar,SysUser::getNickname);
        queryWrapper.last("limit 1");

        return sysUserMapper.selectOne(queryWrapper);
    }

    @Override
    public Result findUserByToken(String token) {
        /**
         * 1.token合法性校验
         *      是否为空 解析是否成功 redis是否存在
         * 2.失败 返回错误
         * 3.如果成功 返回loginUserVo
         */
        if(StringUtils.isBlank(token)){
            return Result.fail(ErrorCode.TOKEN_ERROR.getCode(), ErrorCode.TOKEN_ERROR.getMsg());
        }

        //解析token
        SysUser sysUser = loginService.checkToken(token);
        LoginUserVo loginUserVo = new LoginUserVo();
        loginUserVo.setId(String.valueOf( sysUser.getId()));
        loginUserVo.setAvatar(sysUser.getAvatar());
        loginUserVo.setNickname(sysUser.getNickname());
        loginUserVo.setAccount(sysUser.getAccount());

        return Result.success(loginUserVo);
    }

    @Override
    public SysUser findUserByAccount(String account) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount,account);

        queryWrapper.last("limit 1");

        return sysUserMapper.selectOne(queryWrapper);

    }

    @Override
    public void save(SysUser sysUser) {
        //保存用户 id会自动生成
        //这个地方 默认生成的id是 分布式id 雪花算法
        //分表操作
        this.sysUserMapper.insert(sysUser);
    }

    @Override
    public UserVo findUserVoById(Long authorId) {
        SysUser sysUser = sysUserMapper.selectById(authorId);
        if (sysUser == null){
            sysUser = new SysUser();
            sysUser.setId(1L);
            sysUser.setAvatar("/static/img/logo.b3a48c0.png");
            sysUser.setNickname("码神之路");
        }
        UserVo userVo = new UserVo();
        userVo.setAvatar(sysUser.getAvatar());
        userVo.setNickname(sysUser.getNickname());
        userVo.setId(String.valueOf(sysUser.getId()));
        return userVo;
    }
}

package com.rg.smarts.domain.user.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rg.smarts.domain.user.entity.User;
import com.rg.smarts.infrastructure.common.DeleteRequest;
import com.rg.smarts.interfaces.dto.user.UserQueryRequest;
import com.rg.smarts.interfaces.dto.user.UserUpdateRequest;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 用户服务
 */
public interface UserDomainService{

    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request
     * @return 脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);



    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);


    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);


    /**
     * 获取查询条件
     *
     * @param userQueryRequest
     * @return
     */
    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);

    /**
     * 根据id获取用户
     * @param id
     * @return
     */
    User getUserById(long id);


    /**
     * 分页获取用户列表（仅管理员）
     * @param current
     * @param size
     * @return
     */
    Page<User> listUserByPage(long current,long size, QueryWrapper<User> queryWrapper);


    Boolean removeById(long id);
    Boolean updateById(User user);

}

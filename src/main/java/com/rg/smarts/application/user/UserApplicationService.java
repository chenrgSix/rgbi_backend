package com.rg.smarts.application.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rg.smarts.domain.user.entity.User;
import com.rg.smarts.infrastructure.common.DeleteRequest;
import com.rg.smarts.interfaces.dto.user.*;
import com.rg.smarts.interfaces.vo.LoginUserVO;
import com.rg.smarts.interfaces.vo.UserVO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * 用户服务
 */
public interface UserApplicationService{

    /**
     * 用户注册
     * @return 新用户 id
     */
    long userRegister(UserRegisterRequest userRegisterRequest);

    /**
     * 用户登录
     * @param request
     * @return 脱敏后的用户信息
     */
    LoginUserVO userLogin(UserLoginRequest userLoginRequest, HttpServletRequest request);



    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);
    /**
     * 检查用户积分是否充足
     *
     * @param request
     * @return
     */
    Boolean checkUserPoints(HttpServletRequest request);
    /**
     * 检查用户积分是否充足
     *
     * @param user
     * @return
     */
    Boolean checkUserPoints(User user);

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     * 是否为管理员
     *
     * @param user
     * @return
     */
    boolean isAdmin(User user);

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 获取脱敏的已登录用户信息
     *
     * @return
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 获取脱敏的用户信息
     *
     * @param user
     * @return
     */
    UserVO getUserVO(User user);

    /**
     * 获取脱敏的用户信息
     *
     * @param userList
     * @return
     */
    List<UserVO> getUserVO(List<User> userList);

    /**
     * 获取查询条件
     *
     * @param userQueryRequest
     * @return
     */
    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);

    /**
     * 删除用户
     * @param deleteRequest
     * @return
     */
    Boolean deleteUser( DeleteRequest deleteRequest);

    /**
     * 更新用户
     * @param userUpdateRequest
     * @return
     */
    Boolean updateUser( UserUpdateRequest userUpdateRequest);

    /**
     * 根据id获取用户
     * @param id
     * @return
     */
    User getUserById(long id);
    /**
     * 根据id获取脱敏用户
     * @param id
     * @return
     */
    UserVO getUserByIdVo( long id);

    /**
     * 分页获取用户列表（仅管理员）
     * @param userQueryRequest
     * @return
     */
    Page<User>  listUserByPage(UserQueryRequest userQueryRequest);
    Page<UserVO>  listUserVOByPage(UserQueryRequest userQueryRequest);

    /**
     * 更新自己的信息
     * @param userUpdateMyRequest
     * @param request
     * @return
     */
    Boolean updateMyUser(UserUpdateMyRequest userUpdateMyRequest,
                         HttpServletRequest request);
}

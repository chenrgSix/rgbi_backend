package com.rg.smarts.application.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rg.smarts.domain.score.entity.Score;
import com.rg.smarts.application.user.UserApplicationService;
import com.rg.smarts.domain.user.service.UserDomainService;
import com.rg.smarts.domain.user.constant.UserConstant;
import com.rg.smarts.infrastructure.common.DeleteRequest;
import com.rg.smarts.infrastructure.exception.BusinessException;
import com.rg.smarts.infrastructure.exception.ThrowUtils;
import com.rg.smarts.interfaces.dto.user.*;
import com.rg.smarts.interfaces.vo.LoginUserVO;
import com.rg.smarts.infrastructure.common.ErrorCode;
import com.rg.smarts.infrastructure.mapper.UserMapper;
import com.rg.smarts.domain.user.entity.User;
import com.rg.smarts.interfaces.vo.UserVO;
import com.rg.smarts.application.score.ScoreApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户服务实现
 * 如需调用其他服务，应在应用层，而不能在领域层
 */
@Service
@Slf4j
public class UserApplicationServiceImpl  implements UserApplicationService {

    @Resource
    private UserDomainService userDomainService;
    @Resource
    private ScoreApplicationService scoreApplicationService;

    @Transactional
    @Override
    public long userRegister(UserRegisterRequest userRegisterRequest) {
       ThrowUtils.throwIf(userRegisterRequest==null,ErrorCode.PARAMS_ERROR);
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        User.validUserRegister(userAccount,userPassword,checkPassword);
        synchronized (userAccount.intern()) {
            long userId = userDomainService.userRegister(userAccount, userPassword, checkPassword);
            Score score = new Score();
            score.setUserId(userId);
            score.setScoreTotal(20L);
            boolean save = scoreApplicationService.save(score);
            if (!save) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
            }
            return userId;
        }

    }

    @Override
    public LoginUserVO userLogin(UserLoginRequest userLoginRequest, HttpServletRequest request) {
        // 1. 校验
        ThrowUtils.throwIf(userLoginRequest==null,ErrorCode.PARAMS_ERROR);
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        User.validUserLogin(userAccount,userPassword);
        return userDomainService.userLogin(userAccount,userPassword,request);
    }

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        return userDomainService.getLoginUser(request);
    }

    @Override
    public Boolean checkUserPoints(HttpServletRequest request) {
        User loginUser = getLoginUser(request);
        return checkUserPoints(loginUser);
    }
    @Override
    public Boolean checkUserPoints(User user) {
        Long userPoints = scoreApplicationService.getUserPoints(user.getId());
        return userPoints != null && userPoints > 0;
    }
    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    @Override
    public boolean isAdmin(HttpServletRequest request) {
        // 仅管理员可查询
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User user = (User) userObj;
        return user.isAdmin();
    }

    @Override
    public boolean isAdmin(User user) {
        return user.isAdmin();
    }

    /**
     * 用户注销
     *
     * @param request
     */
    @Override
    public boolean userLogout(HttpServletRequest request) {
        ThrowUtils.throwIf(request==null,ErrorCode.PARAMS_ERROR);
        return userDomainService.userLogout(request);
    }

    @Override
    public LoginUserVO getLoginUserVO(User user) {
        return userDomainService.getLoginUserVO(user);
    }

    @Override
    public UserVO getUserVO(User user) {
        return userDomainService.getUserVO(user);
    }

    @Override
    public List<UserVO> getUserVO(List<User> userList) {
        return userDomainService.getUserVO(userList);
    }

    @Override
    public QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {
        ThrowUtils.throwIf(userQueryRequest==null,ErrorCode.PARAMS_ERROR);
        return userDomainService.getQueryWrapper(userQueryRequest);

    }

    @Override
    public Boolean deleteUser(DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return userDomainService.removeById(deleteRequest.getId());
    }

    @Override
    public Boolean updateUser(UserUpdateRequest userUpdateRequest) {
        if (userUpdateRequest == null || userUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userUpdateRequest, user);
        boolean result = userDomainService.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return true;
    }

    @Override
    public User getUserById(long id) {
        return userDomainService.getUserById(id);
    }

    @Override
    public UserVO getUserByIdVo(long id) {
        return userDomainService.getUserByIdVo(id);
    }

    @Override
    public Page<User> listUserByPage(UserQueryRequest userQueryRequest) {
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        QueryWrapper<User> queryWrapper = userDomainService.getQueryWrapper(userQueryRequest);
        return userDomainService.listUserByPage(current,size,queryWrapper);
    }

    @Override
    public Page<UserVO> listUserVOByPage(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        QueryWrapper<User> queryWrapper = userDomainService.getQueryWrapper(userQueryRequest);
        return userDomainService.listUserVOByPage(current,size,queryWrapper);
    }

    @Override
    public Boolean updateMyUser(UserUpdateMyRequest userUpdateMyRequest, HttpServletRequest request) {

        if (userUpdateMyRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userDomainService.getLoginUser(request);
        User user = new User();
        BeanUtils.copyProperties(userUpdateMyRequest, user);
        user.setId(loginUser.getId());

        boolean result = userDomainService.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return result;
    }
}

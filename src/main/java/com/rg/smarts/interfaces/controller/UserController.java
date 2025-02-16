package com.rg.smarts.interfaces.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rg.smarts.interfaces.dto.user.*;
import com.rg.smarts.interfaces.vo.LoginUserVO;
import com.rg.smarts.infrastructure.annotation.AuthCheck;
import com.rg.smarts.infrastructure.common.BaseResponse;
import com.rg.smarts.infrastructure.common.DeleteRequest;
import com.rg.smarts.infrastructure.common.ErrorCode;
import com.rg.smarts.infrastructure.common.ResultUtils;
import com.rg.smarts.domain.user.constant.UserConstant;
import com.rg.smarts.infrastructure.exception.BusinessException;
import com.rg.smarts.infrastructure.exception.ThrowUtils;
import com.rg.smarts.domain.user.entity.User;
import com.rg.smarts.interfaces.vo.UserVO;
import com.rg.smarts.application.user.UserApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;



/**
 * 用户接口
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserApplicationService userApplicationService;

    /**
     * 用户注册
     *
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        long result = userApplicationService.userRegister(userRegisterRequest);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest
     * @param request
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        LoginUserVO loginUserVO = userApplicationService.userLogin(userLoginRequest,request);
        return ResultUtils.success(loginUserVO);
    }


    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        boolean result = userApplicationService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    @GetMapping("/get/login")
    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request) {
        User user = userApplicationService.getLoginUser(request);
        return ResultUtils.success(userApplicationService.getLoginUserVO(user));
    }

    // endregion

    // region 增删改查

//    /**
//     * 创建用户
//     *
//     * @param userAddRequest
//     * @param request
//     * @return
//     */
//    @PostMapping("/add")
//    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
//    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest, HttpServletRequest request) {
//        if (userAddRequest == null) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        User user = new User();
//        BeanUtils.copyProperties(userAddRequest, user);
//        // 默认密码 12345678
//        String defaultPassword = "12345678";
//        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + defaultPassword).getBytes());
//        user.setUserPassword(encryptPassword);
//        boolean result = userApplicationService.save(user);
//        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
//        return ResultUtils.success(user.getId());
//    }

    /**
     * 删除用户
     *
     * @param deleteRequest
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest) {
        boolean b = userApplicationService.deleteUser(deleteRequest);
        return ResultUtils.success(b);
    }

    /**
     * 更新用户
     *
     * @param userUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {
        Boolean b = userApplicationService.updateUser(userUpdateRequest);
        return ResultUtils.success(b);
    }

    /**
     * 根据 id 获取用户（仅管理员）
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<User> getUserById(long id) {
        User user = userApplicationService.getUserById(id);
        return ResultUtils.success(user);
    }

    /**
     * 根据 id 获取包装类
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<UserVO> getUserVOById(long id) {
        UserVO userByIdVo = userApplicationService.getUserByIdVo(id);
        return ResultUtils.success(userByIdVo);
    }

    /**
     * 分页获取用户列表（仅管理员）
     *
     * @param userQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<User>> listUserByPage(@RequestBody UserQueryRequest userQueryRequest,
            HttpServletRequest request) {
        Page<User> userPage = userApplicationService.listUserByPage(userQueryRequest);
        return ResultUtils.success(userPage);
    }

    /**
     * 分页获取用户封装列表
     *
     * @param userQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<UserVO>> listUserVOByPage(@RequestBody UserQueryRequest userQueryRequest,
            HttpServletRequest request) {
        Page<UserVO> userVOPage = userApplicationService.listUserVOByPage(userQueryRequest);
        return ResultUtils.success(userVOPage);
    }

    /**
     * 更新个人信息
     *
     * @param userUpdateMyRequest
     * @param request
     * @return
     */
    @PostMapping("/update/my")
    public BaseResponse<Boolean> updateMyUser(@RequestBody UserUpdateMyRequest userUpdateMyRequest,
            HttpServletRequest request) {
        Boolean b = userApplicationService.updateMyUser(userUpdateMyRequest,request);
        return ResultUtils.success(b );
    }
}

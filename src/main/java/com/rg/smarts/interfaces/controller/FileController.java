package com.rg.smarts.interfaces.controller;

import com.rg.smarts.application.file.service.FileUploadApplicationService;
import com.rg.smarts.domain.user.constant.UserConstant;
import com.rg.smarts.domain.user.valueobject.UserRoleEnum;
import com.rg.smarts.infrastructure.annotation.AuthCheck;
import com.rg.smarts.infrastructure.common.BaseResponse;
import com.rg.smarts.infrastructure.common.ResultUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件接口
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class FileController {

    @Resource
    private FileUploadApplicationService fileUploadApplicationService;

    /**
     * 文件上传
     *
     * @param multipartFile
     * @param request
     * @return
     */
    @PostMapping("/upload/minio")
    @AuthCheck(mustRole = UserConstant.USER_LOGIN_STATE)
    public BaseResponse<String> uploadFileMinio(@RequestPart("file") MultipartFile multipartFile,
                                                @RequestPart(name = "desc", required = false) String desc,
                                                HttpServletRequest request) {
        String result = fileUploadApplicationService.uploadPictureFile(multipartFile, desc, request);
        return ResultUtils.success(result);
    }

    /**
     * 上传文档，为知识库服务
     * @param multipartFile
     * @param desc
     * @param request
     * @return
     */
    @PostMapping("/upload/document")
    @AuthCheck(mustRole = UserConstant.USER_LOGIN_STATE)
    public BaseResponse<String> uploadDocument(@RequestPart("file") MultipartFile multipartFile,
                                                @RequestPart(name = "desc", required = false) String desc,
                                                HttpServletRequest request) {
        String result = fileUploadApplicationService.uploadDocumentFile(multipartFile, desc, request);
        return ResultUtils.success(result);
    }
}

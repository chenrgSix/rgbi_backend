package com.rg.smarts.application.file.service.impl;

import com.rg.smarts.application.file.service.FileUploadApplicationService;
import com.rg.smarts.application.user.UserApplicationService;
import com.rg.smarts.domain.file.constant.FileConstant;
import com.rg.smarts.domain.file.entity.FileUpload;
import com.rg.smarts.domain.file.service.FileUploadDomainService;
import com.rg.smarts.domain.user.entity.User;
import com.rg.smarts.infrastructure.common.ErrorCode;
import com.rg.smarts.infrastructure.exception.ThrowUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 16152
 * @description 针对表【file_upload(文件表)】的数据库操作Service
 * @createDate 2025-03-14 21:31:16
 */
@Service
public class FileUploadApplicationServiceImpl implements FileUploadApplicationService {
    @Resource
    private FileUploadDomainService fileUploadDomainService;
    @Resource
    private UserApplicationService userApplicationService;


    @Override
    public String uploadPictureFile(MultipartFile multipartFile,
                                    String desc,
                                    HttpServletRequest request) {
        User loginUser = userApplicationService.getLoginUser(request);
        fileUploadDomainService.validPictureFile(multipartFile);
        FileUpload fileUpload = fileUploadDomainService.uploadFile(multipartFile, loginUser.getId(), FileConstant.USER_PICTURE_BUCKET_NAME, desc);
        return fileUpload.getPath();
    }

    @Override
    public FileUpload uploadDocumentFile(MultipartFile multipartFile,
                                     String desc,
                                     HttpServletRequest request) {
        User loginUser = userApplicationService.getLoginUser(request);
        fileUploadDomainService.validDocumentFile(multipartFile);
        return fileUploadDomainService.uploadFile(multipartFile, loginUser.getId(), FileConstant.DOC_BUCKET_NAME, desc);
    }
    @Override
    public String  getFilePathById(Long id) {
        ThrowUtils.throwIf(id==null, ErrorCode.PARAMS_ERROR);
        return fileUploadDomainService.getFilePathById(id);
    }
    @Override
    public FileUpload  getFileById(Long id) {
        ThrowUtils.throwIf(id==null, ErrorCode.PARAMS_ERROR);
        return fileUploadDomainService.getFileById(id);
    }
    @Override
    public void deleteFile(Long userId, String fileName) {
        fileUploadDomainService.deleteFile(userId, fileName);
    }
}

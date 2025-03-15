package com.rg.smarts.application.file.service.impl;

import com.rg.smarts.application.embedding.service.EmbeddingApplicationService;
import com.rg.smarts.application.file.service.FileUploadApplicationService;
import com.rg.smarts.application.user.UserApplicationService;
import com.rg.smarts.domain.file.constant.FileConstant;
import com.rg.smarts.domain.file.entity.FileUpload;
import com.rg.smarts.domain.file.service.FileUploadDomainService;
import com.rg.smarts.domain.user.entity.User;
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
    @Resource
    private EmbeddingApplicationService embeddingApplicationService;
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
    public String uploadDocumentFile(MultipartFile multipartFile,
                                     String desc,
                                     HttpServletRequest request) {
        User loginUser = userApplicationService.getLoginUser(request);
        fileUploadDomainService.validDocumentFile(multipartFile);
        FileUpload fileUpload = fileUploadDomainService.uploadFile(multipartFile, loginUser.getId(), FileConstant.DOC_BUCKET_NAME, desc);
        // 补充文档向量化
        embeddingApplicationService.loadDocument(fileUpload.getPath(),fileUpload.getFileSuffix());
        return fileUpload.getPath();
    }
}

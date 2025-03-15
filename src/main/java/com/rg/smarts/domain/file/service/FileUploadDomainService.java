package com.rg.smarts.domain.file.service;

import com.rg.smarts.domain.file.entity.FileUpload;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
* @author 16152
* @description 针对表【file_upload(文件表)】的数据库操作Service
* @createDate 2025-03-14 21:31:16
*/
public interface FileUploadDomainService {

    @Transactional(rollbackFor = Exception.class)
    FileUpload uploadFile(MultipartFile multipartFile,
                          Long userId, String bucketName, String desc);

    void validDocumentFile(MultipartFile multipartFile);

    void validPictureFile(MultipartFile multipartFile);
}

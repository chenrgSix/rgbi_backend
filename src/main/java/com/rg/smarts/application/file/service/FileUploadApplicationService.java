package com.rg.smarts.application.file.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

/**
* @author 16152
* @description 针对表【file_upload(文件表)】的数据库操作Service
* @createDate 2025-03-14 21:31:16
*/
public interface FileUploadApplicationService{


    String uploadPictureFile(MultipartFile multipartFile,
                             String desc,
                             HttpServletRequest request);

    String uploadDocumentFile(MultipartFile multipartFile,
                              String desc,
                              HttpServletRequest request);
}

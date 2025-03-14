package com.rg.smarts.domain.file.service.impl;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.StrUtil;
import com.rg.smarts.domain.file.constant.DocConstant;
import com.rg.smarts.domain.file.entity.FileUpload;
import com.rg.smarts.domain.file.repository.FileUploadRepository;
import com.rg.smarts.domain.file.service.FileUploadDomainService;
import com.rg.smarts.infrastructure.common.ErrorCode;
import com.rg.smarts.infrastructure.exception.BusinessException;
import com.rg.smarts.infrastructure.utils.MinioUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;


/**
* @author 16152
* @description 针对表【file_upload(文件表)】的数据库操作Service
* @createDate 2025-03-14 21:31:16
*/
@Slf4j
@Service
public class FileUploadDomainServiceImpl implements FileUploadDomainService {
    @Resource
    private FileUploadRepository fileUploadRepository;


    @Resource
    private MinioUtil minioUtil;
    /**
     * 文件上传
     * @param multipartFile
     * @param userId
     * @return 地址
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String uploadFile(MultipartFile multipartFile,
                             Long userId, String desc) {
        // TODO 判断是否上传过已有的相同文件
        // 文件目录：根据业务、用户来划分
        String uuid = RandomStringUtils.randomAlphanumeric(8);
        String displayFileName = multipartFile.getOriginalFilename();
        String filename = uuid + "-" + displayFileName;
        String filepath = String.format("%s/%s", userId, filename);
        File file = null;
        try {
            // 上传文件
            file = File.createTempFile(filepath, null);
            multipartFile.transferTo(file);
            String uploadUrl = minioUtil.upload(filepath, file);
            if(StrUtil.isBlank(uploadUrl)){
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
            }
            // 填充属性
            String type =FileTypeUtil.getType(file);
            filename = FileNameUtil.getName(file);
            long fileSize = FileUtil.size(file);

            FileUpload fileUpload = new FileUpload();

            fileUpload.setUserId(userId);
            fileUpload.setFileName(filename);
            fileUpload.setDisplayName(displayFileName);
            fileUpload.setFileSuffix(type);
            fileUpload.setFileSize(fileSize);
            fileUpload.setPath(uploadUrl);
            fileUpload.setFileDesc(desc);
            fileUploadRepository.save(fileUpload);
            // 返回可访问地址
            return uploadUrl;
        } catch (Exception e) {
            log.error("file upload error, filepath = " + filepath, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
        } finally {
            if (file != null) {
                // 删除临时文件
                boolean delete = file.delete();
                if (!delete) {
                    log.error("file delete error, filepath = {}", filepath);
                }
            }
        }
    }
    /**
     * 校验头像文件
     * @param multipartFile
     * */
    @Override
    public void validDocumentFile(MultipartFile multipartFile) {
        // 文件大小
        long fileSize = multipartFile.getSize();
        // 文件后缀
        String fileSuffix = FileUtil.getSuffix(multipartFile.getOriginalFilename());

        if (fileSize > (DocConstant.DOC_BASE_SIZE*DocConstant.DOC_MAX_SIZE)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, String.format("文件大小不能超过%d M!", DocConstant.DOC_BASE_SIZE));
        }
        if (!DocConstant.DOC_TYPES.contains(fileSuffix)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "无法解析的文件类型");
        }
    }

    /**
     * 校验头像文件
     * @param multipartFile
     * */
    @Override
    public void validPictureFile(MultipartFile multipartFile) {
        // 文件大小
        long fileSize = multipartFile.getSize();
        // 文件后缀
        String fileSuffix = FileUtil.getSuffix(multipartFile.getOriginalFilename());
        final long ONE_M = 1024 * 1024L;
        if (fileSize > ONE_M) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件大小不能超过 1M");
        }
        if (!Arrays.asList("jpeg", "jpg", "svg", "png", "webp").contains(fileSuffix)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件类型错误");
        }
    }
}

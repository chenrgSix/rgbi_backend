package com.rg.smarts.infrastructure.repository.file;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rg.smarts.domain.file.entity.FileUpload;
import com.rg.smarts.domain.file.repository.FileUploadRepository;
import com.rg.smarts.infrastructure.mapper.FileUploadMapper;
import org.springframework.stereotype.Service;

/**
* @author 16152
* @description 针对表【file_upload(文件表)】的数据库操作Service实现
* @createDate 2025-03-14 21:31:16
*/
@Service
public class FileUploadRepositoryImpl extends ServiceImpl<FileUploadMapper, FileUpload>
    implements FileUploadRepository {

}





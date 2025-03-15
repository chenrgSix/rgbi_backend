package com.rg.smarts.domain.file.constant;

import java.util.Arrays;
import java.util.List;

public interface FileConstant {

    long DOC_BASE_SIZE = 1024 * 1024L; // 文件基本单位
    long DOC_MAX_SIZE =  20; // 单个文件最大尺寸
    List<String> DOC_TYPES =  Arrays.asList("doc", "docx", "ppt", "pptx", "xls", "xlsx", "pdf") ;
    String USER_PICTURE_BUCKET_NAME = "image";
    String DOC_BUCKET_NAME = "document";

}

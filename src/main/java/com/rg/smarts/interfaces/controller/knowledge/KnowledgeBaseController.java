package com.rg.smarts.interfaces.controller.knowledge;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rg.smarts.application.knowledge.service.KnowledgeBaseApplicationService;
import com.rg.smarts.infrastructure.annotation.AuthCheck;
import com.rg.smarts.infrastructure.common.BaseResponse;
import com.rg.smarts.infrastructure.common.ResultUtils;
import com.rg.smarts.interfaces.dto.knowledge.KnowledgeAddDocumentRequest;
import com.rg.smarts.interfaces.dto.knowledge.KnowledgeBaseAddRequest;
import com.rg.smarts.interfaces.dto.knowledge.KnowledgeBaseQueryRequest;
import com.rg.smarts.interfaces.vo.KnowledgeBaseVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: czr
 * @CreateTime: 2025-03-16
 * @Description: 知识库相关
 */
@RestController
@RequestMapping("/knowledge")
@Slf4j
public class KnowledgeBaseController {
    @Resource
    private KnowledgeBaseApplicationService knowledgeBaseApplicationService;

    @PostMapping(value = "/add")
    @AuthCheck
    public BaseResponse<Boolean> addKnowledgeBase(@RequestBody KnowledgeBaseAddRequest knowledgeBaseAddRequest,HttpServletRequest request) {
        Boolean b = knowledgeBaseApplicationService.addKnowledgeBase(knowledgeBaseAddRequest,request);
        return ResultUtils.success(b);
    }

    /**
     * 分页查询（普通用户）
     * @param knowledgeBaseQueryRequest
     * @param request
     * @return
     */
    @PostMapping(value = "/list/page")
    public BaseResponse<Page<KnowledgeBaseVO>> listKnowledgeBaseByPage(@RequestBody KnowledgeBaseQueryRequest knowledgeBaseQueryRequest, HttpServletRequest request) {
        Page<KnowledgeBaseVO> result = knowledgeBaseApplicationService.listKnowledgeBaseByPage(knowledgeBaseQueryRequest,request);
        return ResultUtils.success(result);
    }

    @GetMapping(value = "/get")
    public BaseResponse<KnowledgeBaseVO> getKnowledgeBaseById(Long id, HttpServletRequest request) {
        KnowledgeBaseVO result = knowledgeBaseApplicationService.getKnowledgeBaseById(id,request);
        return ResultUtils.success(result);
    }

    /**
     * todo 补充知识库删除
     */

    /**
     * 上传文档
     * @param multipartFile
     * @param request
     * @return
     */
    @PostMapping("/upload")
    public BaseResponse<Boolean> addDocument(@RequestPart("file") MultipartFile multipartFile,
                                             KnowledgeAddDocumentRequest knowledgeAddDocumentRequest,
                                             HttpServletRequest request) {
//        knowledgeAddDocumentRequest.setKbId(1901258879333826561L);
        Boolean result = knowledgeBaseApplicationService.addDocument(multipartFile, knowledgeAddDocumentRequest, request);
        return ResultUtils.success(result);
    }
}

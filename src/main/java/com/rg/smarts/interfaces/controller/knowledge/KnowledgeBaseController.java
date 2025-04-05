package com.rg.smarts.interfaces.controller.knowledge;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rg.smarts.application.knowledge.service.KnowledgeBaseApplicationService;
import com.rg.smarts.infrastructure.annotation.AuthCheck;
import com.rg.smarts.infrastructure.common.BaseResponse;
import com.rg.smarts.infrastructure.common.ResultUtils;
import com.rg.smarts.interfaces.dto.knowledge.KnowledgeAddDocumentRequest;
import com.rg.smarts.interfaces.dto.knowledge.KnowledgeBaseAddRequest;
import com.rg.smarts.interfaces.dto.knowledge.KnowledgeBaseQueryRequest;
import com.rg.smarts.interfaces.dto.knowledge.KnowledgeDocumentQueryRequest;
import com.rg.smarts.interfaces.vo.knowledge.DocumentInfoVO;
import com.rg.smarts.interfaces.vo.knowledge.KnowledgeBaseVO;
import com.rg.smarts.interfaces.vo.knowledge.KnowledgeDocumentVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    @GetMapping(value = "/get/selectable")
    public BaseResponse<List<KnowledgeBaseVO>> getSelectableKnowledgeBaseByUserId( HttpServletRequest request) {
        List<KnowledgeBaseVO> selectableKnowledgeBaseByUserId = knowledgeBaseApplicationService.getSelectableKnowledgeBaseByUserId(request);
        return ResultUtils.success(selectableKnowledgeBaseByUserId);
    }
    /**
     * todo 补充知识库删除
     */

    @PostMapping(value = "/doc/page")
    public BaseResponse<Page<KnowledgeDocumentVO>> listDocByPage(@RequestBody KnowledgeDocumentQueryRequest knowledgeDocumentQueryRequest, HttpServletRequest request) {
        Page<KnowledgeDocumentVO> result = knowledgeBaseApplicationService.listDocByPage(knowledgeDocumentQueryRequest,request);
        return ResultUtils.success(result);
    }

    @PostMapping (value = "/doc/info")
    public BaseResponse<DocumentInfoVO> getDocumentInfo(@RequestBody KnowledgeDocumentQueryRequest knowledgeDocumentQueryRequest, HttpServletRequest request) {
        DocumentInfoVO result = knowledgeBaseApplicationService.getDocumentInfo(knowledgeDocumentQueryRequest,request);
        return ResultUtils.success(result);
    }

    @PostMapping (value = "/doc/delete")
    public BaseResponse<Boolean> deleteDocument(Long docId, HttpServletRequest request) {
        Boolean result = knowledgeBaseApplicationService.deleteDocument(docId, request);
        return ResultUtils.success(result);
    }


    /**
     * 上传文档
     * @param multipartFile
     * @param request
     * @return
     */

    @PostMapping("/doc/upload")
    public BaseResponse<Boolean> addDocument(@RequestPart("file") MultipartFile multipartFile,
                                             KnowledgeAddDocumentRequest knowledgeAddDocumentRequest,
                                             HttpServletRequest request) {
//        knowledgeAddDocumentRequest.setKbId(1901258879333826561L);
        Boolean result = knowledgeBaseApplicationService.addDocument(multipartFile, knowledgeAddDocumentRequest, request);
        return ResultUtils.success(result);
    }

    /**
     * 解析文档
     * @param request
     * @return
     */
    @PostMapping("/doc/load")
    public BaseResponse<Boolean> loadDocument(Long docId,HttpServletRequest request) {
        Boolean result = knowledgeBaseApplicationService.loadDocument(docId,request);
        return ResultUtils.success(result);
    }
}

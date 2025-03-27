package com.rg.smarts.application.knowledge.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rg.smarts.application.file.service.FileUploadApplicationService;
import com.rg.smarts.application.knowledge.dto.DocumentInfoDTO;
import com.rg.smarts.application.knowledge.dto.DocumentKnn;
import com.rg.smarts.application.knowledge.service.KnowledgeBaseApplicationService;
import com.rg.smarts.application.user.UserApplicationService;
import com.rg.smarts.domain.file.entity.FileUpload;
import com.rg.smarts.domain.knowledge.entity.KnowledgeBase;
import com.rg.smarts.domain.knowledge.entity.KnowledgeDocument;
import com.rg.smarts.domain.knowledge.service.KnowledgeBaseDomainService;
import com.rg.smarts.domain.knowledge.valueobject.DocumentStatusEnum;
import com.rg.smarts.domain.knowledge.valueobject.KBStatusEnum;
import com.rg.smarts.domain.user.entity.User;
import com.rg.smarts.infrastructure.common.ErrorCode;
import com.rg.smarts.infrastructure.constant.CommonConstant;
import com.rg.smarts.infrastructure.exception.BusinessException;
import com.rg.smarts.infrastructure.exception.ThrowUtils;
import com.rg.smarts.infrastructure.utils.SqlUtils;
import com.rg.smarts.interfaces.dto.knowledge.KnowledgeAddDocumentRequest;
import com.rg.smarts.interfaces.dto.knowledge.KnowledgeBaseAddRequest;
import com.rg.smarts.interfaces.dto.knowledge.KnowledgeBaseQueryRequest;
import com.rg.smarts.interfaces.dto.knowledge.KnowledgeDocumentQueryRequest;
import com.rg.smarts.interfaces.vo.knowledge.DocumentInfoVO;
import com.rg.smarts.interfaces.vo.knowledge.KnowledgeBaseVO;
import com.rg.smarts.interfaces.vo.knowledge.KnowledgeDocumentVO;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author: czr
 * @CreateTime: 2025-03-16
 * @Description:
 */
@Service
public class KnowledgeBaseApplicationServiceImpl implements KnowledgeBaseApplicationService {
    @Resource
    private KnowledgeBaseDomainService knowledgeBaseDomainService;
    @Resource
    private UserApplicationService userApplicationService;

    @Resource
    private FileUploadApplicationService fileUploadApplicationService;
    @Resource
    private TransactionTemplate transactionTemplate;

    @Override
    public Boolean addKnowledgeBase(KnowledgeBaseAddRequest knowledgeBaseAddRequest, HttpServletRequest request) {
        User loginUser = userApplicationService.getLoginUser(request);
        String title = knowledgeBaseAddRequest.getTitle();
        Integer isPublic = knowledgeBaseAddRequest.getIsPublic();
        ThrowUtils.throwIf(StringUtils.isBlank(title), ErrorCode.PARAMS_ERROR,"知识库名称不可为空");
        if (isPublic!=null){
            ThrowUtils.throwIf(KBStatusEnum.getEnumByValue(isPublic)==null, ErrorCode.PARAMS_ERROR);
        }
        KnowledgeBase knowledgeBase = new KnowledgeBase();
        BeanUtils.copyProperties(knowledgeBaseAddRequest, knowledgeBase);
        knowledgeBase.setUserId(loginUser.getId());
        return knowledgeBaseDomainService.addKnowledgeBase(knowledgeBase);
    }

    /**
     * 获取内容检索器
     * @param kbId
     * @return
     */
    @Override
    public ContentRetriever getContentRetriever(Long kbId,Long userId){
        return knowledgeBaseDomainService.getContentRetriever(kbId,userId);
    }

    /**
     * 获取用户可见的知识库分页
     * @param knowledgeBaseQueryRequest
     * @param request
     * @return
     */
    @Override
    public Page<KnowledgeBaseVO> listKnowledgeBaseByPage(KnowledgeBaseQueryRequest knowledgeBaseQueryRequest, HttpServletRequest request) {
        long current = knowledgeBaseQueryRequest.getCurrent();
        long size = knowledgeBaseQueryRequest.getPageSize();
        QueryWrapper<KnowledgeBase> queryWrapper = getQueryKnowledgeBaseWrapper(knowledgeBaseQueryRequest);
        // 可见范围（自己创建的，或者公开的）
        User loginUser = userApplicationService.getLoginUser(request);
        queryWrapper.and(i->{
            i.eq("userId", loginUser.getId()).or().eq("isPublic", KBStatusEnum.PUBLIC.getValue());
        });
        Page<KnowledgeBase> knowledgeBasePage = knowledgeBaseDomainService.getKnowledgeBasePage(new Page<>(current, size),queryWrapper);
        List<KnowledgeBase> records = knowledgeBasePage.getRecords();
        List<KnowledgeBaseVO> knowledgeBaseVOList = records.stream().map(knowledgeBase -> {
            KnowledgeBaseVO knowledgeBaseVO = new KnowledgeBaseVO();
            BeanUtils.copyProperties(knowledgeBase, knowledgeBaseVO);
            return knowledgeBaseVO;
        }).toList();
        Page<KnowledgeBaseVO> knowledgeBaseVOPage = new Page<>(current, size, knowledgeBasePage.getTotal());
        knowledgeBaseVOPage.setRecords(knowledgeBaseVOList);
        return knowledgeBaseVOPage;
    }

    /**
     * 根据id获取知识库的信息
     * @param id
     * @param request
     * @return
     */
    @Override
    public KnowledgeBaseVO getKnowledgeBaseById(Long id, HttpServletRequest request) {
        KnowledgeBase knowledgeBase = knowledgeBaseDomainService.getKnowledgeBaseById(id);
        ThrowUtils.throwIf(knowledgeBase==null, ErrorCode.NOT_FOUND_ERROR);
        User user = userApplicationService.getLoginUser(request);
        ThrowUtils.throwIf(!knowledgeBase.isVisible(user.getId()), ErrorCode.NO_AUTH_ERROR);
        KnowledgeBaseVO knowledgeBaseVO = new KnowledgeBaseVO();
        BeanUtils.copyProperties(knowledgeBase, knowledgeBaseVO);
        return knowledgeBaseVO;
    }


    @Override
    public KnowledgeBaseVO getKnowledgeDocumentById(Long id, HttpServletRequest request) {
        return null;
    }

    /**
     * 添加知识库文档
     * @param multipartFile
     * @param knowledgeAddDocumentRequest
     * @param request
     * @return
     */
    @Override
    public Boolean addDocument(MultipartFile multipartFile, KnowledgeAddDocumentRequest knowledgeAddDocumentRequest, HttpServletRequest request) {
        User loginUser = userApplicationService.getLoginUser(request);
        Long userId = loginUser.getId();
        Long kbId = knowledgeAddDocumentRequest.getKbId();
        ThrowUtils.throwIf(kbId==null, ErrorCode.PARAMS_ERROR,"缺失知识库id");
        KnowledgeBase knowledgeBaseById = knowledgeBaseDomainService.getKnowledgeBaseById(kbId);

        Integer docNum = knowledgeBaseById.getDocNum();
        ThrowUtils.throwIf(!knowledgeBaseById.isVisible(userId), ErrorCode.NO_AUTH_ERROR,"无此知识库的操作权限");
        return transactionTemplate.execute(status -> {
            FileUpload fileUpload = null;
            try {
                fileUpload = fileUploadApplicationService.uploadDocumentFile(multipartFile, "",kbId);
                return knowledgeBaseDomainService.addKnowledgeDocument(
                        kbId,
                        userId,
                        fileUpload.getId(),
                        fileUpload.getFileSuffix(),
                        docNum);
            } catch (Exception e) {
                status.setRollbackOnly();
                // 删除已经保存的文档
                if (fileUpload != null) {
                    fileUploadApplicationService.deleteFileForMinio(loginUser.getId(), fileUpload.getFileName());
                }
            }
            return null;
        });
    }

    //  进行向量化
    @Override
    public Boolean loadDocument(Long docId,HttpServletRequest request){
        ThrowUtils.throwIf(docId==null, ErrorCode.PARAMS_ERROR);
        KnowledgeDocument knowledgeDocument = knowledgeBaseDomainService.getKnowledgeDocumentById(docId);
        if(!DocumentStatusEnum.READY.getValue().equals(knowledgeDocument.getStatus())&&
                !DocumentStatusEnum.FAIL.getValue().equals(knowledgeDocument.getStatus())){
            String text = DocumentStatusEnum.getEnumByValue(knowledgeDocument.getStatus()).getText();
            throw new BusinessException(ErrorCode.OPERATION_ERROR,text);
        }
        Boolean allowed = isAllowed(knowledgeDocument.getKbId(), request);
        ThrowUtils.throwIf(!allowed, ErrorCode.NO_AUTH_ERROR);
        String filePath = fileUploadApplicationService.getFilePathById(knowledgeDocument.getFileId());
        knowledgeBaseDomainService.loadDocument(knowledgeDocument, filePath);
        return true;
    }

    @Override
    public Page<KnowledgeDocumentVO> listDocByPage(KnowledgeDocumentQueryRequest knowledgeDocumentQueryRequest, HttpServletRequest request) {
        Long kbId = knowledgeDocumentQueryRequest.getKbId();
        ThrowUtils.throwIf(kbId==null, ErrorCode.PARAMS_ERROR,"知识库id为空");
        Boolean allowed = isAllowed(kbId, request);
        ThrowUtils.throwIf(!allowed, ErrorCode.PARAMS_ERROR,"知识库id为空");
        int current = knowledgeDocumentQueryRequest.getCurrent();
        int pageSize = knowledgeDocumentQueryRequest.getPageSize();
        QueryWrapper<KnowledgeDocument> queryKnowledgeDocWrapper = getQueryKnowledgeDocWrapper(knowledgeDocumentQueryRequest);
        Page<KnowledgeDocument> knowledgeDocPage=knowledgeBaseDomainService.getKnowledgeDocPage(new Page<>(current, pageSize),queryKnowledgeDocWrapper);

        List<KnowledgeDocument> records = knowledgeDocPage.getRecords();
        List<KnowledgeDocumentVO> knowledgeDocumentVOList = records.stream().map(knowledgeDocument -> {
            KnowledgeDocumentVO knowledgeDocumentVO = new KnowledgeDocumentVO();
            BeanUtils.copyProperties(knowledgeDocument, knowledgeDocumentVO);
            FileUpload fileUpload = fileUploadApplicationService.getFileById(knowledgeDocument.getFileId());
            knowledgeDocumentVO.setDisplayName(fileUpload.getDisplayName());
            knowledgeDocumentVO.setFileSize(fileUpload.getFileSize());
            return knowledgeDocumentVO;
        }).toList();
        Page<KnowledgeDocumentVO> knowledgeDocumentVOPage = new Page<>(current, pageSize, knowledgeDocPage.getTotal());
        knowledgeDocumentVOPage.setRecords(knowledgeDocumentVOList);
        return knowledgeDocumentVOPage;
    }

    @Override
    public DocumentInfoVO getDocumentInfo(KnowledgeDocumentQueryRequest knowledgeDocumentQueryRequest, HttpServletRequest request) {
        Long docId = knowledgeDocumentQueryRequest.getId();
        int pageSize = knowledgeDocumentQueryRequest.getPageSize();
        int current = knowledgeDocumentQueryRequest.getCurrent();
        KnowledgeDocument document = knowledgeBaseDomainService.getKnowledgeDocumentById(docId);
        Boolean allowed = isAllowed(document.getKbId(), request);
        ThrowUtils.throwIf(!allowed, ErrorCode.NO_AUTH_ERROR);
        DocumentInfoDTO documentInfoDTO = knowledgeBaseDomainService.getDocumentInfo(document,current,pageSize);
        return documentInfoDTO.toVO();
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteDocument(long docId, HttpServletRequest request) {
        Long userId  = userApplicationService.getLoginUser(request).getId();
        KnowledgeDocument knowledgeDocument = knowledgeBaseDomainService.deleteDocument(docId,userId);
        fileUploadApplicationService.deleteFileUpload(knowledgeDocument.getKbId(),knowledgeDocument.getFileId());
        return true;
    }

    /**
     * 返回向量查询的结果
     * @param userId
     * @param kbId
     * @param search
     * @return
     */
    @Override
    public List<DocumentKnn> searchDocumentChunk(Long userId, Long kbId, String search){
        Boolean verifyResult = knowledgeBaseDomainService.verifyIdentity(kbId, userId);
        ThrowUtils.throwIf(!verifyResult, ErrorCode.NO_AUTH_ERROR);
        List<DocumentKnn> documentKnns = knowledgeBaseDomainService.searchDocumentChunk(search, kbId);
        return documentKnns;
    }
    private QueryWrapper<KnowledgeBase> getQueryKnowledgeBaseWrapper(KnowledgeBaseQueryRequest knowledgeBase) {
        ThrowUtils.throwIf(knowledgeBase == null,ErrorCode.PARAMS_ERROR);
        String sortField = knowledgeBase.getSortField();
        String sortOrder = knowledgeBase.getSortOrder();
        String title = knowledgeBase.getTitle();

        String ingestModelName = knowledgeBase.getIngestModelName();
        QueryWrapper<KnowledgeBase> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(title),"title",title);
        queryWrapper.eq(StringUtils.isNotBlank(ingestModelName), "ingestModelName", ingestModelName);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }
    private QueryWrapper<KnowledgeDocument> getQueryKnowledgeDocWrapper(KnowledgeDocumentQueryRequest knowledgeDocumentQueryRequest) {
        ThrowUtils.throwIf(knowledgeDocumentQueryRequest == null,ErrorCode.PARAMS_ERROR);
        String sortField = knowledgeDocumentQueryRequest.getSortField();
        String sortOrder = knowledgeDocumentQueryRequest.getSortOrder();
        Long kbId = knowledgeDocumentQueryRequest.getKbId();
        Integer status = knowledgeDocumentQueryRequest.getStatus();
        QueryWrapper<KnowledgeDocument> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(kbId!=null, "kbId", kbId);
        queryWrapper.eq(status!=null, "status", status);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 是否允许操作
     * @param kb_id
     * @param request
     * @return
     */
    private Boolean isAllowed(Long kb_id,HttpServletRequest request){
        Long userId  = userApplicationService.getLoginUser(request).getId();
        return knowledgeBaseDomainService.verifyIdentity(kb_id,userId);
    }
}

package com.rg.smarts.infrastructure.repository.dialogues;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rg.smarts.domain.dialogues.entity.Dialogues;
import com.rg.smarts.domain.dialogues.repository.DialoguesRepository;
import com.rg.smarts.infrastructure.mapper.DialoguesMapper;
import org.springframework.stereotype.Repository;

@Repository
public class DialoguesRepositoryImpl extends ServiceImpl<DialoguesMapper, Dialogues> implements DialoguesRepository {
}

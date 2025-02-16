package com.rg.smarts.infrastructure.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rg.smarts.domain.user.entity.User;
import com.rg.smarts.domain.user.repository.UserRepository;
import com.rg.smarts.infrastructure.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class UserRepositoryImpl extends ServiceImpl<UserMapper, User> implements UserRepository {
}

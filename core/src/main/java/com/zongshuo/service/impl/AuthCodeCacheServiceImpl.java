package com.zongshuo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zongshuo.mapper.AuthCodeCacheMapper;
import com.zongshuo.model.AuthCodeCacheModel;
import com.zongshuo.service.AuthCodeCacheService;
import org.springframework.stereotype.Service;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-26
 * @Time: 23:29
 * @Description:
 */
@Service
public class AuthCodeCacheServiceImpl extends ServiceImpl<AuthCodeCacheMapper, AuthCodeCacheModel> implements AuthCodeCacheService {
}

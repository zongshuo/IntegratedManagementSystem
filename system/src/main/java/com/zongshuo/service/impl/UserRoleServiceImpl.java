package com.zongshuo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zongshuo.entity.UserRole;
import com.zongshuo.mapper.UserRoleMapper;
import com.zongshuo.model.UserRoleModel;
import com.zongshuo.service.UserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-22
 * @Time: 11:26
 * @Description:
 */
@Service
@Slf4j
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRoleModel> implements UserRoleService {
}

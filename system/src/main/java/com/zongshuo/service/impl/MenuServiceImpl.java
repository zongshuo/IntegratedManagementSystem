package com.zongshuo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zongshuo.entity.Menu;
import com.zongshuo.mapper.MenuMapper;
import com.zongshuo.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-20
 * @Time: 20:14
 * @Description:
 */
@Service
@Slf4j
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService{
}

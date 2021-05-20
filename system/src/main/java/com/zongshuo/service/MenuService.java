package com.zongshuo.service;

import com.zongshuo.BO.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-20
 * @Time: 20:11
 * @Description: 系统菜单操作服务类
 */
public interface MenuService extends JpaRepository<Menu, Integer> {
}

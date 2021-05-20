package com.zongshuo.service.impl;

import com.zongshuo.BO.Menu;
import com.zongshuo.service.MenuService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-20
 * @Time: 20:15
 * @Description:
 */
@SpringBootTest
class MenuServiceImplTest {
    @Autowired
    private MenuService menuService;

    @BeforeEach
    void setUp() {
        assertNotNull(menuService);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void save(){
        Menu menu = new Menu();
        menuService.save(menu);
    }
}
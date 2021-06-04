package com.zongshuo.service.impl;

import com.zongshuo.entity.Menu;
import com.zongshuo.service.MenuService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Base64;

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
//        Menu menu = new Menu();
//        menuService.save(menu);


    }

    public static void main(String[] args) {
        String encode = new String(Base64.getEncoder().encode("0ZW?&PmZ_彩色显示器-secret_slot".getBytes()));
        System.out.println(encode);
    }
}
package com.zongshuo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zongshuo.model.MenuModel;
import com.zongshuo.model.UserModel;
import com.zongshuo.service.MenuModelService;
import com.zongshuo.service.UserModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;


/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-6-12
 * @Time: 16:54
 * @Description:
 * 用于系统启动后初始化系统参数
 */
@Component
public class AppInitRunner implements ApplicationRunner {
    @Autowired
    private UserModelService userModelService;
    @Autowired
    private MenuModelService menuModelService;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        try{

            //新增超级管理员
            UserModel adminUser = userModelService.getOne(
                    new QueryWrapper<UserModel>().lambda().eq(UserModel::getUsername, Contains.SYS_ADMIN_NAME));
            if (adminUser == null) {
                adminUser = new UserModel();
                adminUser.setUsername(Contains.SYS_ADMIN_NAME);
                adminUser.setPassword(new BCryptPasswordEncoder().encode(Contains.SYS_ADMIN_NAME));
                adminUser.setNickName("系统管理员");
                adminUser.setEmail("1737290510@qq.com");
                adminUser.setCreateTime(new Date());
                userModelService.save(adminUser);
            }

            //新增系统管理菜单
            MenuModel menuModel = new MenuModel();
            menuModel.setParentId(0);
            menuModel.setTitle(Contains.MENU_SYS_ADMIN_NAME);
            menuModel.setSortNumber(1);
            menuModelService.addMenu(menuModel);

            //新增角色
        }catch (Exception e){
            //TODO 处理异常
        }
    }
}

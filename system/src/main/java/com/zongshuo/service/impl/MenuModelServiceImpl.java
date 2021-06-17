package com.zongshuo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zongshuo.entity.Role;
import com.zongshuo.mapper.MenuModelMapper;
import com.zongshuo.model.MenuModel;
import com.zongshuo.model.RoleModel;
import com.zongshuo.service.MenuModelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-20
 * @Time: 20:14
 * @Description:
 * 系统菜单服务实现类
 */
@Slf4j
@Service
public class MenuModelServiceImpl extends ServiceImpl<MenuModelMapper, MenuModel> implements MenuModelService {
    @Autowired(required = false)
    private MenuModelMapper menuModelMapper;


    @Override
    public void addMenu(MenuModel menuModel) throws IllegalAccessException{
        QueryWrapper<MenuModel> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(MenuModel::getTitle, menuModel.getTitle())
                .eq(MenuModel::getParentId, menuModel.getParentId());
        if (baseMapper.selectCount(wrapper) > 0){
            throw new IllegalAccessException("菜单已存在！");
        }

        baseMapper.insert(menuModel);
    }

    @Override
    public List<MenuModel> getMenuListByRole(RoleModel role) {
        return getMenuListByRole(Arrays.asList(role));
    }


    @Override
    public List<MenuModel> getMenuListByRole(List<RoleModel> roleModels) {
        if (roleModels == null || roleModels.size() < 1) return new ArrayList<>(0);
        List<MenuModel> menuModels = menuModelMapper.getMenusByRoleIds(roleModels);
        if (menuModels == null) return new ArrayList<>(0);

        return menuModels;
    }

    @Override
    public List<MenuModel> toMenuTree(List<MenuModel> menuModels, Integer parentId) {
        List<MenuModel> childes = new ArrayList<>();
        for (MenuModel menu : menuModels){
            if (menu.getParentId().equals(parentId)){
                menu.setChildes(toMenuTree(menuModels, menu.getMenuId()));
                childes.add(menu);
            }
        }
        return childes;
    }
}

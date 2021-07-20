package com.zongshuo.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.*;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-6-15
 * @Time: 15:56
 * @Description:
 */
@Data
@Entity
@Table(name = "SYS_ROLE_MENU")
public class RoleMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="role_id", insertable = false, updatable = false, referencedColumnName = "id")
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", insertable = false, updatable = false, referencedColumnName = "id")
    private Menu menu;
}

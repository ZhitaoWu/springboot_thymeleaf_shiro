package com.wzt.service;

import com.wzt.entity.Perms;
import com.wzt.entity.User;

import java.util.List;

/**
 * @User:Tao
 * @date:2021/1/7
 */

public interface UserService {
    // 注册方法
    void register(User user);

    // 根据用户名查询业务的方法
    User findByUserName(String username);

    // 根据用户名查询所有角色
    User findRolesByUserName(String username);

    // 根据角色id查询权限集合
    List<Perms> findPermsByRoleId(Integer id);
}

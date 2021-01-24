package com.wzt.dao;

import com.wzt.entity.Perms;
import com.wzt.entity.User;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * @User:Tao
 * @date:2021/1/24
 */
/*添加了@Mapper注解之后这个接口在编译时会生成相应的实现类
 *
 * 需要注意的是：这个接口中不可以定义同名的方法，因为会生成相同的id
 * 也就是说这个接口是不支持重载的
*/
@Mapper
//@Repository("userDao")
public interface UserDao {
    // 保存用户信息
    void save(User user);

    // 根据身份信息认证的方法
    User findByUserName(String username);

    // 根据用户名查询所有角色
    User findRolesByUserName(String username);

    // 根据角色id查询权限集合
    List<Perms> findPermsByRoleId(Integer id);
}

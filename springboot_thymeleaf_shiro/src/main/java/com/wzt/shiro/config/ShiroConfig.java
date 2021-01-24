package com.wzt.shiro.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.wzt.shiro.cache.RedisCacheManager;
import com.wzt.shiro.realms.CustomerRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @User:Tao
 * @date:2021/1/23
 * 整合shiro框架相关的配置类
 */
@Configuration
public class ShiroConfig {

    @Bean(name = "shiroDialect")
    public ShiroDialect shiroDialect(){
        return new ShiroDialect();
    }

    // 1.创建shiroFilter 拦截器
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(SecurityManager securityManager) {
        // 创建shiro的filter
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 注入安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 配置系统受限资源
        // 配置系统公共资源
        //Map<String,String> map = new HashMap<String,String>();

        // 必须为LinkedHashMap 否则anon不生效
        // anon 要在 authc 之前配置
        Map<String,String> map = new LinkedHashMap<>();

        map.put("/login.html","anon");//anon 设置为公共资源  放行资源放在下面
        map.put("/index.html","anon");
        map.put("/register.html","anon");
        map.put("/user/getImage","anon");
        map.put("/user/register","anon");
        map.put("/user/registerView","anon");
        map.put("/user/login","anon");

        map.put("/**","authc");//authc 请求这个资源需要认证和授权

        // 默认认证界面路径
        shiroFilterFactoryBean.setLoginUrl("/user/loginView");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);

        return shiroFilterFactoryBean;
    }

    // 2.创建web管理器
    @Bean
    public DefaultWebSecurityManager getSecurityManager(@Qualifier("myRealm") Realm realm) {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setRealm(realm);
        return defaultWebSecurityManager;
    }

    // 3.创建自定义realm
    @Bean(name = "myRealm")
    public Realm getRealm() {
        CustomerRealm customerRealm = new CustomerRealm();

        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher(); // 设置hashed凭证匹配器
        hashedCredentialsMatcher.setHashAlgorithmName("md5");// 设置md5加密
        hashedCredentialsMatcher.setHashIterations(1024);// 设置散列次数
        customerRealm.setCredentialsMatcher(hashedCredentialsMatcher);

        //开启缓存管理
        customerRealm.setCacheManager(new RedisCacheManager());
        customerRealm.setCachingEnabled(true);//开启全局缓存
        customerRealm.setAuthenticationCachingEnabled(true);//认证认证缓存
        customerRealm.setAuthenticationCacheName("authenticationCache");
        customerRealm.setAuthorizationCachingEnabled(true);//开启授权缓存
        customerRealm.setAuthorizationCacheName("authorizationCache");

        return customerRealm;
    }
}

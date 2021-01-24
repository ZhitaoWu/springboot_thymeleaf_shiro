package com.wzt.controller;

import com.wzt.entity.User;
import com.wzt.service.UserService;
import com.wzt.utils.VerifyCodeUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @User:Tao
 * @date:2021/1/6
 */
@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 跳转到登录页面
     * @return
     */
    @RequestMapping("loginView")
    public String login() {
        System.out.println("跳转到login页面");
        return "login";
    }

    /**
     * 跳转到注册界面
     * @return
     */
    @RequestMapping("registerView")
    public String register() {
        System.out.println("跳转到register页面");
        return "register";
    }


    /**
     * 生成验证码
     * @param session
     * @param response
     * @throws IOException
     */
    @RequestMapping("getImage")
    public void getImage(HttpSession session, HttpServletResponse response) throws IOException {
        // 生成验证码
        String code = VerifyCodeUtils.generateVerifyCode(4);// 4位
        // 验证码放入session
        session.setAttribute("code",code);
        // 验证码放入图片
        ServletOutputStream outputStream = response.getOutputStream();
        response.setContentType("image/png");
        VerifyCodeUtils.outputImage(160,40,outputStream,code);

    }

    /**
     * 用户认证
     * @param user
     * @return
     */
    @RequestMapping("register")
    public String register(User user) {
        try {
            userService.register(user);
            return "redirect:/user/loginView";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/user/registerView";
        }
    }

    /**
     * 用于处理身份认证
     * @param username
     * @param password
     * @return
     */
    @RequestMapping("login")
    public String login(String username,String password,String code,HttpSession session) {

        // 比较验证码
        String codes = (String) session.getAttribute("code");
        try {
            if (codes.equalsIgnoreCase(code)) {
                // 获取主体对象
                Subject subject = SecurityUtils.getSubject();
                subject.login(new UsernamePasswordToken(username,password));
                return "redirect:/index";
            } else {
                throw new RuntimeException("验证码错误！");
            }
        }catch (UnknownAccountException e) {
            e.printStackTrace();
            System.out.println("用户名错误。");
        } catch (IncorrectCredentialsException e) {
            e.printStackTrace();
            System.out.println("用户密码错误。");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return "redirect:/user/loginView";
    }

    /**
     * 注销当前账户
     * @return
     */
    @RequestMapping("logout")
    public String logout() {
        // 获取主体对象
        Subject subject = SecurityUtils.getSubject();
        // 退出登录
        subject.logout();
        return "redirect:/user/loginView";
    }


}

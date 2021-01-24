package com.wzt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @User:Tao
 * @date:2021/1/22
 * 主页的控制器
 */
@Controller
public class IndexController {

    @RequestMapping("index")
    public String getIndex() {
        System.out.println("springboot thymeleaf shiro hello.");
        return "index";
    }

}

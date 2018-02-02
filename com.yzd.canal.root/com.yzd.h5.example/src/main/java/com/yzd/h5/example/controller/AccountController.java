package com.yzd.h5.example.controller;

import com.yzd.h5.example.utils.cookieExt.CookieUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("account")
public class AccountController {
    @RequestMapping("doLogin")
    @ResponseBody
    public String doLogin(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil cookieUtil = new CookieUtil(request, response);
        cookieUtil.addCookie("test","value",1000);
        return "account/doLogin";
    }
    @RequestMapping("doLogout")
    @ResponseBody
    public String doLogout(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil cookieUtil = new CookieUtil(request, response);
        cookieUtil.deleteCookie("test");
        return "account/doLogout";
    }
}

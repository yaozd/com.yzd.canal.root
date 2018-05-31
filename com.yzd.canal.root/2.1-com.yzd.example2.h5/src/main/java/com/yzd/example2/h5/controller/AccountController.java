package com.yzd.example2.h5.controller;

import com.yzd.example2.h5.utils.fastjson.FastJsonUtil;
import com.yzd.example2.h5.utils.sessionExt.CurrentUser;
import com.yzd.example2.h5.utils.sessionExt.LoginSessionUtil;
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
        CurrentUser currentUser =new CurrentUser(1000L,"yzd");
        LoginSessionUtil.setLogin(currentUser,request,response);
        return "account/doLogin";
    }
    @RequestMapping("doLogout")
    @ResponseBody
    public String doLogout(HttpServletRequest request, HttpServletResponse response) {
        LoginSessionUtil.logout(request, response);
        return "account/doLogout";
    }
    @RequestMapping("getCurrentUser")
    @ResponseBody
    public String getCurrentUser() {
        CurrentUser user= LoginSessionUtil.getCurrentUser();
        String userStr= FastJsonUtil.serialize(user);
        return "account/getCurrentUser:"+userStr;
    }
}
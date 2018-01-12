package com.yzd.h5.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @RequestMapping(value = {"", "/index"})
    @ResponseBody
    public String index() {
        return "index/index";
    }
}

package com.yzd.example2.h5.controller;

import com.yzd.example2.h5.dao.entity.Other01;
import com.yzd.example2.h5.service.inf.IOther01ServiceInf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/other01")
public class Other01Controller {
    @Autowired
    IOther01ServiceInf iOther01ServiceInf;
    @RequestMapping("/doSelectAll")
    @ResponseBody
    public List<Other01> doSelectAll() {
        List<Other01> other01List = iOther01ServiceInf.selectAll();
        return other01List;
    }
}
package com.yzd.h5.example.controller;

import com.yzd.h5.example.dao.entity.Other01;
import com.yzd.h5.example.dao.mapper.Other01Mapper;
import com.yzd.h5.example.service.inf.IOther01ServiceInf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/other01")
public class Other01Controller {
    @Autowired
    IOther01ServiceInf iOther01ServiceInf;
    @RequestMapping("/doInsert")
    @ResponseBody
    public String doInsert() {
        SimpleDateFormat sdf = new SimpleDateFormat("DD-hhmmss");
        String date=sdf.format(new Date());
        Other01 item=new Other01();
        item.setName(date);
        iOther01ServiceInf.insertSelective(item);
        return "ok";
    }
    @RequestMapping("/doSelectAll")
    @ResponseBody
    public List<Other01> doSelectAll() {
        List<Other01> other01List= iOther01ServiceInf.selectAll();
        return other01List;
    }
}

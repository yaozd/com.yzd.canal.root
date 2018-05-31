package com.yzd.example2.h5.controller;

import com.yzd.example2.h5.dao.entity.Other02;
import com.yzd.example2.h5.service.inf.IOther02ServiceInf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/other02")
public class Other02Controller {

    @Autowired
    IOther02ServiceInf iOther02ServiceInf;

    @RequestMapping("/doSelectById")
    @ResponseBody
    public Other02 doSelectById() {
        Other02 item = iOther02ServiceInf.selectByPrimaryKey(6);
        return item;
    }
    @RequestMapping("/doUpdateById")
    @ResponseBody
    public Integer doUpdateById() {
        SimpleDateFormat sdf = new SimpleDateFormat("DD-hhmmss");
        String date = sdf.format(new Date());
        Other02 item = new Other02();
        item.setUid(6);
        item.setName(date);
        Integer count = iOther02ServiceInf.updateByPrimaryKeySelective(item);
        return count;
    }
}

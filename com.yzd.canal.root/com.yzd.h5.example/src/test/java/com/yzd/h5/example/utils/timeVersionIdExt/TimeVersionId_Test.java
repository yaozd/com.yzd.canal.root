package com.yzd.h5.example.utils.timeVersionIdExt;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class TimeVersionId_Test {
    @Test
    public void t1(){
        //Java,SET集合的 元素不能重复
        Set<String> set = new HashSet<String>();
        set.add("1");
        set.add("1");
        //用set.size()方法来获取这个集合的大小时返回也是1。
        System.out.println(set.size());
        for (int i = 0; i <1000 ; i++) {
            String val=TimeVersionId.getInstance().getTimeVersion();
            System.out.println(val);
            set.add(val);
        }
        System.out.println(set.size());
    }
}

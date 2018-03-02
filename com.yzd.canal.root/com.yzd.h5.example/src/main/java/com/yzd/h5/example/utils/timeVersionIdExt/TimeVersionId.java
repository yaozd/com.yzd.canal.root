package com.yzd.h5.example.utils.timeVersionIdExt;

public class TimeVersionId {
    private static TimeVersionId ourInstance = new TimeVersionId();

    public static TimeVersionId getInstance() {
        return ourInstance;
    }

    private TimeVersionId() {

    }
    private IPIdGenerator ipIdGenerator=new IPIdGenerator();
    //通过twitter的snowflake算法解决数据时间戳重复问题
    public String getTimeVersion() {
        Long val=ipIdGenerator.generateId().longValue();
        //十进制转为36进制
        //18长度ID变为12位长度
        return Long.toString(val, 36);
    }
}

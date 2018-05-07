### 项目意义：通过Redis+Canal实现数据的全量缓存
### 0.数据库结构
```
.doc->tb_other_test.sql
```
### 1.REDIS相关操作
```
Redis操作字符串工具类封装：http://fanshuyao.iteye.com/blog/2326221
Redis操作Hash工具类封装：http://fanshuyao.iteye.com/blog/2327134
Redis操作List工具类封装：http://fanshuyao.iteye.com/blog/2327137
Redis操作Set工具类封装：http://fanshuyao.iteye.com/blog/2327228
```
### 2.KEY作用对应表
KEY |类型|作用
---|---|---
EvictAllKeyList | List| 删除旧的资源时间戳版本对应的所有缓存
ExpireAllKeySet | Set| 保证所有的SaveAllKeySet都设置了过期时间
SaveAllKeySet | Set| 保存资源时间戳版本对应的所有缓存
P01.Timestamp:userId:1000| String| 私有资源时间戳版本
P01.UserBaseInfo.1000:1520ben35yo3y:XX| String| 私有资源缓存KEY的名称
P01.Timestamp:publicNormal| String| 公有资源时间戳版本
P01.Other1SelectAll:1dben35yo3y8:XX| String| 公有资源缓存KEY的名称

### 3.通过twitter的snowflake算法解决数据时间戳重复问题
```
根据机器IP获取工作进程Id,如果线上机器的IP二进制表示的最后10位不重复
1dben35yo3y8
//十进制转为36进制
//18长度ID变为12位长度
```
### 4.缓存数据的生命周期必须要小于资源时间戳版本的生命周期
```
getCachedWrapperByTimestampKeyValue方法中的定义如下：
//暂时取keyExpireSec过期时间 与 timestampTtl剩余时间（秒）中最小的值
//todo 理论上讲keyExpireSec过期时间应该等于timestampTtl剩余时间（秒）； 
```
### 5.Redis删除所有Key
```
删除所有Key
删除所有Key，可以使用Redis的flushdb和flushall命令
//删除当前数据库中的所有Key
flushdb
//删除所有数据库中的key
flushall
注：keys 指令可以进行模糊匹配，但如果 Key 含空格，就匹配不到了，暂时还没发现好的解决办法。
http://ssuupv.blog.163.com/blog/static/1461567220135610456193/
```
### 5.参考：基于Canal使用HTTP协议跨机房公网同步数据
```
基于Canal使用HTTP协议跨机房公网同步数据
http://www.caosh.me/be-tech/canal-sync-db-over-http/
```
### 6.canal版本选择-选型时间：20180504
```
//备注：
//目前阿里云RDS-mysql-5.6版本，可以使用 canal-server-v1.0.24,(canal-v1.0.25版本则不可用)
//canal-server-v1.0.24 下载地址：https://github.com/alibaba/canal/releases
//canal-server-v1.0.24版本-百度云有备份
//
```
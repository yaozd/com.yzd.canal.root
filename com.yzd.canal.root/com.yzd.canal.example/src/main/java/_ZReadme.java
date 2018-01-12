
/**
 缘起
 公司有一个app客户端，在调取某些数据时，需要从不同的项目中获得数据，并处理这些数据，导致显示到客户端非常慢，现需要对这个问题进行优化。数据库大都是mysql服务器，所以选用了适合它的开源框架canal来处理数据。

 在使用canal监听mysql的二进制日志的过程中，发现可用性还是比较强的，优点如下
 能够针对不同的表进行监听（订阅）
 Canal客户端出现异常时，服务器可以将产品记录，重启客户端时可以将未消费的数据继续消费，做到不丢失数据（服务端记录未消费的产品）
 Canal服务端出现异常，导致客户端无法消费数据时， 重启Canal服务器可以从出现异常时产生的数据，也可以做到不丢失数据。（服务端可以从上次无法生产的地方继续生产数据）
 */
//备注：
//目前阿里云RDS-mysql-5.6版本，可以使用 canal-server-v1.0.24,(canal-v1.0.25版本则不可用)
//canal-server-v1.0.24 下载地址：https://github.com/alibaba/canal/releases
//canal-server-v1.0.24版本-百度云有备份
//
//T4--定制化监听服务可作参考
//参考：
/**
 1,Canal记录mysql的binlog日志监听
 http://www.wangminli.com/?p=1162
 2,阿里巴巴开源项目: 基于mysql数据库binlog的增量订阅&消费
 http://agapple.iteye.com/blog/1796633
 Canal Client API
 https://yq.aliyun.com/articles/14530
 alibaba/canal-release版本
 https://github.com/alibaba/canal/releases?spm=5176.100239.blogcont14530.5.12c3a604CRMAAr
 */
//
//CANAL-配置实例
/**
 #################################################
 ## mysql serverId
 canal.instance.mysql.slaveId = 1234

 #master-主数据库的信息
 # position info
 canal.instance.master.address = 192.168.3.30:3306
 canal.instance.master.journal.name =
 canal.instance.master.position =
 canal.instance.master.timestamp =
 #standby-备用数据库的信息
 #canal.instance.standby.address =
 #canal.instance.standby.journal.name =
 #canal.instance.standby.position =
 #canal.instance.standby.timestamp =

 #用户名与密码
 # username/password
 canal.instance.dbUsername = canal
 canal.instance.dbPassword = canal
 canal.instance.defaultDatabaseName =
 canal.instance.connectionCharset = UTF-8

 # table regex-过滤白名单
 #canal.instance.filter.regex = .*\\..*
 canal.instance.filter.regex = tb_other_test\\..*
 # table black regex-过滤黑名单
 canal.instance.filter.black.regex = tb_my_test\\..*,tb_third_test\\..*

 #################################################
 */

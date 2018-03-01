# webFunction

web端一些简单的web功能,实现了如下接口

1.snmp、syslog接口
	主要功能：
	获取：从数据库中读入设置参数
	更新：发送消息给日志服务器进行更新操作(相应地在日志服务器端添加对应的消息、操作以及错误码)
	
2.服务器进程检测工具接口
	主要功能：
	查询：发送消息给服务器进程检测工具（cpst）进行查询操作
	操作：发送消息给服务器进程检测工具（cpst）进行更新操作

3.自备份配置接口
	主要功能：
	见doc/自备份支持页面配置任务设计书.doc

开发过程中引用的第三方库
    jaxrs-ri  jersey框架
    jackson   jersey生产消费json数据时用户转换
    mybatis   数据库框架
    mysql-connector-java-5.0.8-bin    mybatis需要使用mysql的jdbc
（第一方库不在此目录提供，部署时统一放入tomcat共享库目录）
注：部署过程需要将使用的第三方库放入web目录下的WEB-INF/lib中


src目录：java源码
	cn/com/xxx/web
		pojo：简单对象，用于与json对象自动转换及mybatis映射
		mapper：mybatis映射配置文件
		model：模型相关

WebContent ：网页部署时的根目录
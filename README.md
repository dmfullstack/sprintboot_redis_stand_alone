# SpringBoot 整合redis 单机版—— ###主要为了了解下位图操作
---
## redis安装
摘自 [Linux下redis安装与使用](https://www.cnblogs.com/codersay/p/4301677.html)

redis官网： http://www.redis.io/

最新版本： **4.0.10**

下载到官网即可，安装步骤如下：
 1. 下载并编译
```` linux
$ wget http://download.redis.io/releases/redis-4.0.10.tar.gz
$ tar -zxvf redis-4.0.10.tar.gz
$ cd redis-4.0.10
$ make

````
 2.编译完成后目录下会出现 redis.conf 及src下redis-serve、redis-cli 将三个文件拷贝到一个文件夹下
````
$ mkdir /usr/redis
$ cp redis.conf /usr/redis
$ cp src/redis-server /usr/redis
$ cp src/redis-cli /usr/redis
````
 3.启动Redis服务
````
$ cd /usr/redis
$ ./redis-server ./redis.conf
````
 4.测试redis启动
````
$ ./redis-cli
127.0.0.1:6379> 
````
此时redis便正常启动了。

存在问题：
* redis只能本机调用  
  当然端口还未开放，端口开放后还需要配置一些信息
  修改redis.conf 
  将bind 127.0.0.1 注释掉即可
* redis启动窗口一直存在  
  修改redis.conf
  找到 daemonize no 修改为yes
* 修改密码  
  修改redis.conf
  requirepass redis123

---  

## 项目整合
**1. pom引入**
````
    <-- 这是我们的主角 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>
    <-- 为了方便写controller调用调试引入 web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
````
**2.连接技术**
  
  【前几天刚看到大牛推送的一个关于redis位图的操作，故自己安装一个单机版与集群版测试下】
  
**1. [如何优雅地使用Redis之位图操作](https://mp.weixin.qq.com/s/DBqBcBoVtZhH8rMUwXubow)**  
**2. [再谈如何优雅地使用Redis之位图操作](https://mp.weixin.qq.com/s/DBqBcBoVtZhH8rMUwXubow)**
  
  一直未使用redis集群版 与 springboot 集成 ，自己搭建一个使用一下

**[遗留问题]**  
  有帖子说集群版使用 RedisTemplate 会有问题，稍后集群搭建起来再对问题进行验证。
  

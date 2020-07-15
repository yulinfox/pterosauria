# 简单的mock server
## 依赖
- mysql
- jdk8
- gradle 4.x，推荐使用4.10
- spring boot 2.1.6.RELEASE

## 打包&启动
### 打包
```
gradle bootJar
```
### 启动
```
java -jar package-name.jar
```

## 其他说明
- 建表语句在sql文件夹下，里面包含两条测试数据
- 当前只实现了异步的get&post请求，其他的有兴趣可以自己补充，在AsyncHandler中
- 配置见application.properties
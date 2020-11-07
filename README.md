# 公共研发小组 web 项目框架模板

## 编译方法

> 所有的命令行在 *nix 系统下执行，如果在 windows 下执行请修改路径分隔符等差异的部分。

编译项目，生成 boot jar，忽略单元测试。

````shell script
./gradlew clean build -x test -info
````

生成的 Jar 在 `backend/build/libs` 下

## 开发模式下前后端分离执行的方法

项目分为 backend 和 frontend 两个目录。

backend 目录下执行：

````shell script
../gradlew bootRun -x test -info
````

持续编译：

````
../gradlew build --continuous -x :frontend:bundle -x test
````

frontend 目录下执行：

````shell script
npm install

npm start -verbose
````

## 代码规范

后端遵从 Java 规范。提交前，需要使用检查代码。

````shell script
./gradlew check
````

前端通过 eslint 保证代码规范：

````shell script
npm run lint
````

提交代码前请按要求调整代码格式。

## Systemd 部署

配置文件：`/etc/systemd/system/boot-react.service`

```text
[Unit]
Description=Boot React
After=syslog.target

[Service]
User=root
Environment='JAVA_HOME=/usr/java/default' 'JAVA_OPTS=-Xmx4g -Xms4g'
ExecStart=/opt/boot-react/boot-react.jar
SuccessExitStatus=143

[Install]
WantedBy=multi-user.target
```

## xxl-job for windows
> 在xxl-job-admin里直接集成了`SampleXxlJob`,打包成 Windows 程序


[原项目](https://github.com/xuxueli/xxl-job)


### 说明
- 打包工具: Launch4j + Inno Setup 6


### 使用
##### 1. 下载安装 
[MyJob-Installer.exe](https://github.com/ts7ming/xxl-job-win/releases)

##### 2. 配置数据库
- 安装完成后打开配置文件目录
- 修改 `config.json` 文件里数据库连接和账户 (确保账户有足够的权限建库)

![](docs/ug01.png)
![](docs/ug02.png)

##### 3. 启动!
- 启动 `MyJob.exe`
- 点击 `初始化数据库` 按钮. 等待执行完成
- 点击 `启动` 按钮.
- 点击 `打开Web` 按钮 或 自行浏览器打开 [http://127.0.0.1:9001/xxl-job-admin](http://127.0.0.1:9001/xxl-job-admin)
- 初始账号: u:admin/p:123456

##### 4. Enjoy
- 用法和原 xxl-job一致
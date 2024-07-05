## xxl-job 微调

[原项目](https://github.com/xuxueli/xxl-job)

### 修改项
- 管理员账号访问 /xxl-job-admin/shutdown 即可关闭调度平台
- executor 连接调度中心 10 次失败后自动关闭 (连接成功重新计数)


### xxl-job on Windows
##### 构建
- 打包工具: Launch4j + Inno Setup 6
- 打包的exe会开启程序奔溃自动重启

#### 使用步骤
1. 下载安装 xxl-job-win.exe
2. 初始化 mysql
3. 修改 配置
4. 启动 xxl-job-admin.exe 和 xxl-job-executor.exe
5. 访问 [localhost:8080/xxl-job-admin/] 初始账号: u:admin/p:123456
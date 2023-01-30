# 部署

配置jar包执行权限，这里将 `aliyun-ddns.jar` 放在 `/opt/aliyun-ddns` 目录下，按实际情况修改

`chmod +x /opt/aliyun-ddns/aliyun-ddns.jar`

## aliyun-ddns.service

`vi /etc/systemd/system/aliyun-ddns.service`

内容

```bash
[Unit]
Description=aliyun-ddns
Documentation=https://www.kangaroohy.com
After=syslog.target

[Service]
User=root
ExecStart=/opt/aliyun-ddns/aliyun-ddns.jar start
ExecReload=/opt/aliyun-ddns/aliyun-ddns.jar restart
ExecStop=/opt/aliyun-ddns/aliyun-ddns.jar stop
SuccessExitStatus=143
Restart=always

[Install]
WantedBy=multi-user.target
```

命令如下

```bash
# 开机自启动
systemctl enable aliyun-ddns

# 启动
systemctl start aliyun-ddns

# 重启
systemctl restart aliyun-ddns

#状态
systemctl status aliyun-ddns

#关闭
systemctl stop aliyun-ddns
```
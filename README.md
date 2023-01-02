# 工程简介

基于阿里云的动态DNS，支持 IPv4 和 IPv6

IPv4 和 IPv6 IP地址查询网址目前默认的 `ipw.cn`，可修改相应配置

项目打包以后，在jar包同级目录创建`config`文件夹，并编写 `application.yml` 配置文件

文件内容如下：

~~~yaml
server:
  port: 8080 # 可选，默认8080

ddns:
  aliyun:
    ipv4-query-domain: https://4.ipw.cn # ipv4查询地址，默认 https://4.ipw.cn
    ipv6-query-domain: https://6.ipw.cn # ipv6查询地址，默认 https://6.ipw.cn
    access-key-id: access-key-id # key-id
    access-key-secret: access-key-secret # secret
    cron: 0 0/3 * * * ? # 默认每三分钟刷新一次
    domain:
      - domain-name: aliyun.asia # 顶级域名
        sub-domain-name: testv4 # 二级域名
        record-type: A # A表示IPv4，AAAA表示IPv6，默认 AAAA
~~~

也可以是 `application.properties` 文件

~~~properties
# 可选，默认8080
server.port=8080

# ipv4查询地址，默认 https://4.ipw.cn
ddns.aliyun.ipv4-query-domain=https://4.ipw.cn
# ipv6查询地址，默认 https://6.ipw.cn
ddns.aliyun.ipv6-query-domain=https://6.ipw.cn
# key-id
ddns.aliyun.access-key-id=access-key-id
# secret
ddns.aliyun.access-key-secret=access-key-secret
# 刷新频次，默认每三分钟刷新一次
ddns.aliyun.cron=0 0/3 * * * ?
# 顶级域名
ddns.aliyun.domain[0].domain-name=aliyun.asia
# 二级域名
ddns.aliyun.domain[0].sub-domain-name=testv4
# A表示IPv4，AAAA表示IPv6，默认 AAAA
ddns.aliyun.domain[0].record-type=A
~~~

启动会自动刷新一次，此后按照指定的刷新频次刷新，默认3分钟一次

强制刷新某个映射，可访问：`http://{ip}:{port}?subDomainName=testv4`

或访问：`http://{ip}:{port}/refresh?subDomainName=testv4`

如果有多个域名，`key-id / key-secret` 自定义成一样的即可，暂不支持配置多个 `key-id / key-secret`

deploy文件夹下为打包好的jar包
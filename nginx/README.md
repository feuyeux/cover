# 安装
http://nginx.org/download/

# 配置
替换nginx.conf

其中ssl_certificate和ssl_certificate_key分别是SSL证书pem和key。

由keystore生成key和pem
```
keytool -importkeystore -srckeystore .keystore -destkeystore server.p12 -srcstoretype jks -deststoretype pkcs12
openssl pkcs12 -in server.p12 -nocerts -out server.key
openssl pkcs12 -in server.p12 -nodes -out server.pem
```

# 验证
https://myServer:8010/rest/users

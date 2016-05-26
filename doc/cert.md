### cas端
```
rm -f /opt/i-cas.*
```
#### 1.自签证书
```
keytool -genkey \
-dname "CN=cas.alibaba.net, OU=iplus, O=cas, L=Aliyun, ST=Alibaba, C=China" \
-alias iplus-cas \
-keyalg RSA \
-keystore /opt/i-cas.keystore \
-keypass ali.mr6 -storepass ali.mr6 \
-validity 3600
```
#### 2.导出自签证书
```
keytool -export \
-alias iplus-cas \
-keystore /opt/i-cas.keystore \
-storepass ali.mr6 \
-rfc -file /opt/i-cas.cer
```
#### 3.cas容器配置
```xml
<Connector port="8443" protocol="org.apache.coyote.http11.Http11Protocol"
   maxThreads="150" SSLEnabled="true" scheme="https" secure="true"
   clientAuth="false" sslProtocol="TLS"
   keystoreFile="/opt/i-cas.keystore" keystorePass="ali.mr6"/>
```


### app端
```
rm -f /opt/i-app.keystore
```
#### 1.自签证书
```
keytool -genkey \
-dname "CN=iplus, OU=iplus, O=iplus, L=Aliyun, ST=Alibaba, C=China" \
-alias iplus-app \
-keyalg RSA \
-keystore /opt/i-app.keystore \
-keypass ali.mr6 -storepass ali.mr6 \
-validity 3600
```

#### 2.导入服务器证书
```
keytool -importcert -noprompt \
-trustcacerts -alias cas.alibaba.net \
-file /opt/i-cas.cer \
-keystore /opt/i-app.keystore \
-storepass ali.mr6 -keypass ali.mr6
```

#### 3.app ssl配置
cover/AppA/src/main/resources/application.properties

```
server.ssl.key-store=/opt/i-app.keystore
server.ssl.key-store-password=ali.mr6
server.ssl.key-password=ali.mr6
```

#### 4.hosts
```
sudo nano /etc/hosts
127.0.0.1 iplus
```

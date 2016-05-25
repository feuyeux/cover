配置
===

# 下载
官网https://www.apereo.org/projects/cas/download-cas

# 安装证书库
```
keytool -genkey -alias tomcat -keyalg RSA
cp .keystore /opt/tomcat-7.0.67/keystore
```
# Tomcat开启SSL
```
<Connector port="8443" protocol="org.apache.coyote.http11.Http11Protocol"
               maxThreads="150" SSLEnabled="true" scheme="https" secure="true"
               clientAuth="false" sslProtocol="TLS"
               keystoreFile="keystore" keystorePass="123456"
               truststoreFile="/usr/java/jdk1.8.0_65/jre/lib/security/cacerts"
    />
```
# 修改
## hosts增加
mySqlServer

配置
===

# 下载
官网https://www.apereo.org/projects/cas/download-cas

# 安装证书库
```
keytool -genkey -alias tomcat -keyalg RSA
cp .keystore /opt/tomcat-7.0.67/keystore
```

# 导出证书，也可通过浏览器下载
keytool -export -alias tomcat -keypass changeit -file server.crt

# 导入到client端
keytool -import -file server.crt -keypass changeit -keystore "%JAVA_HOME%"\jre\lib\security\cacerts  -alias tomcat

# Tomcat开启SSL
```
<Connector port="8443" protocol="org.apache.coyote.http11.Http11Protocol"
               maxThreads="150" SSLEnabled="true" scheme="https" secure="true"
               clientAuth="false" sslProtocol="TLS"
               keystoreFile="keystore" keystorePass="changeit"
               truststoreFile="/usr/java/jdk1.7.0_80/jre/lib/security/cacerts"
    />
```
# 修改
## hosts增加
mySqlServer

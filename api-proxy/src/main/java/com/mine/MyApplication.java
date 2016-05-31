package com.mine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

@SpringBootApplication
@EnableDiscoveryClient
public class MyApplication {
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(MyApplication.class, args);

        /*
        javax.net.ssl.SSLHandshakeException: sun.security.validator.ValidatorException:
        PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException:
        unable to find valid certification path to requested target

        为了解决『PKIX path building failed』这个问题,因为默认要查JRE的信任证书,而应用为此污染JRE证书是得不偿失的
        */
        Environment env = ctx.getEnvironment();
        String trustStore = env.getProperty("server.ssl.trustStore");
        String trustPassword = env.getProperty("server.ssl.trustStorePassword");
        System.setProperty("javax.net.ssl.trustStore", trustStore);
        System.setProperty("javax.net.ssl.trustStorePassword", trustPassword);
    }
}

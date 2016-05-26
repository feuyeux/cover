package com.mine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class MyApplication {
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(MyApplication.class, args);
    }
}
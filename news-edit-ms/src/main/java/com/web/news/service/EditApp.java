package com.web.news.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@MapperScan("com.web.news.mapper")
@EnableEurekaClient
public class EditApp {

    public static void main(String[] args) {
        SpringApplication.run(EditApp.class,args);
    }
}


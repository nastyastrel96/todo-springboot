package com.nastyastrel.springbootrest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableCaching
public class SpringbootRestApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringbootRestApplication.class, args);
    }
}

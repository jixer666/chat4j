package com.abc.chat4j;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.abc.chat4j.*")
public class Chat4jApplication {
    public static void main(String[] args) {
        SpringApplication.run(Chat4jApplication.class, args);
    }
}
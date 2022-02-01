package com.company.springreactive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.company.springreactive.techie")
public class SpringReactiveApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringReactiveApplication.class, args);
    }
}

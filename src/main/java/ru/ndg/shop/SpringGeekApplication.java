package ru.ndg.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "ru.ndg.shop")
public class SpringGeekApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringGeekApplication.class, args);
    }

}

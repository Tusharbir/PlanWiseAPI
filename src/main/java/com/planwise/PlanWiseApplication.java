package com.planwise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class PlanWiseApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlanWiseApplication.class, args);
    }

}

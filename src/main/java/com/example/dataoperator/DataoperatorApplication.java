package com.example.dataoperator;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.dataoperator.dao")
public class DataoperatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataoperatorApplication.class, args);
    }

}

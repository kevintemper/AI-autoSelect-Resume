package com.example.zwtcampuscareerview;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.zwtcampuscareerview.repositories.mapper")
public class ZwtcampuscareerviewApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZwtcampuscareerviewApplication.class, args);
    }

}

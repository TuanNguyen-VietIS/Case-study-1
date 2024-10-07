package com.tun.casestudy1;

import com.tun.casestudy1.service.FileStorageService;
import jakarta.annotation.Resource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CaseStudy1Application implements CommandLineRunner {

    @Resource
    FileStorageService storageService;

    public static void main(String[] args) {
        SpringApplication.run(CaseStudy1Application.class, args);
    }

    @Override
    public void run(String... arg) throws Exception {
        storageService.init();
    }

}

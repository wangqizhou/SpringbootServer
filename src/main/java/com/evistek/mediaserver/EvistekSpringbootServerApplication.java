package com.evistek.mediaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class EvistekSpringbootServerApplication {

    public static void main(String[] args) {
		SpringApplication.run(EvistekSpringbootServerApplication.class, args);
	}

}

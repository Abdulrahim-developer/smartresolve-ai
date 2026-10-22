package com.acedigital.smartresolve;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SmartresolveApplication {

	public static void main(String[] args) {


        SpringApplication.run(SmartresolveApplication.class, args);


	}

}

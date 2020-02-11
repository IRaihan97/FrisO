package com.group20.webservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@EnableJpaAuditing
public class WebserviceApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(WebserviceApplication.class, args);
	}

}

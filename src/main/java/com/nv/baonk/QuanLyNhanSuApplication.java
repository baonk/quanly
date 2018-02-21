package com.nv.baonk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestContextListener;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

@SpringBootApplication
@EnableEncryptableProperties
public class QuanLyNhanSuApplication {
	public static void main(String[] args) {
		SpringApplication.run(QuanLyNhanSuApplication.class, args);
	}
	
	@Bean 
	public RequestContextListener requestContextListener(){
		return new RequestContextListener();
	}
}
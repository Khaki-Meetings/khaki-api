package com.getkhaki.api.bff;

import com.getkhaki.api.bff.config.PropertiesLogger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(Application.class);
		springApplication.addListeners(new PropertiesLogger());
		springApplication.run(args);
	}
}

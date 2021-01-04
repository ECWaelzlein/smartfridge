package de.isemwaf.smartFridge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SmartFridgeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartFridgeApplication.class, args);
	}

}

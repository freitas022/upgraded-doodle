package br.com.freitas.upgradeddoodle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class UpgradeddoodleApplication {

	public static void main(String[] args) {
		SpringApplication.run(UpgradeddoodleApplication.class, args);
	}

}

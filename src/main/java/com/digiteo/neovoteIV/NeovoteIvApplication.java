package com.digiteo.neovoteIV;

import com.digiteo.neovoteIV.model.jpa.data.UserEntity;
import com.digiteo.neovoteIV.model.jpa.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication // (exclude = {SecurityAutoConfiguration.class})
public class NeovoteIvApplication {

	public static void main(String[] args) {
		SpringApplication.run(NeovoteIvApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(UserRepository userRepository){
		return args -> {
			UserEntity pepe = new UserEntity(
					"Pepe",
					"Grillo",
					"pepe123",
					"pepegrillo123@hotmail.com",
					"Pepe2023_",
					"Hombre");

			userRepository.save(pepe);
		};
	}
}

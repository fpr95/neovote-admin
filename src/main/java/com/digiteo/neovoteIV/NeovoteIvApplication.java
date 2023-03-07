package com.digiteo.neovoteIV;

import com.digiteo.neovoteIV.model.jpa.data.Election;
import com.digiteo.neovoteIV.model.jpa.data.UserEntity;
import com.digiteo.neovoteIV.model.jpa.repository.ElectionRepository;
import com.digiteo.neovoteIV.model.jpa.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication // (exclude = {SecurityAutoConfiguration.class})
public class NeovoteIvApplication {

	public static void main(String[] args) {
		SpringApplication.run(NeovoteIvApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(UserRepository userRepository, ElectionRepository electionRepository){
		return args -> {
			UserEntity pepe = new UserEntity(
					"Pepe",
					"Grillo",
					"pepe123",
					"pepegrillo123@hotmail.com",
					"Pepe2023_",
					"Hombre");

			userRepository.save(pepe);

			/*
			Election election = new Election(
					"Vertedero sector El Retiro",
					"Reclamo junta de vecinos N°23",
					"salud,ambiental,social",
					LocalDateTime.of(2023, 03, 15, 13, 00),
					LocalDateTime.of(2023, 03, 15, 16, 00),
					"Soluciones para la acumulación de desechos en El Retiro.");

			electionRepository.save(election);

							El día 25/02, la junta de vecinos N° 52 de Quilpué (Peyronet #212) nos hizo llegar " +
							"una carta firmada por 98 de 116 miembros activos del comité buscando subsanar un conflicto de ámbito " +
							"sanitario: La acumulación de basura y desechos en el vertedero ubicado en la intersección de Bellochio " +
							"con Dr. Salas. Como municipio tenemos presente el timpo que esto lleva escalando y las consecuencias que acarrea " +
							"para los vecin@s del sector, por ende, presentamos un nuevo proceso abierto con distintas soluciones ya " +
							"revisadas por nuestro equipo para ser ejecutadas una vez se cierren las urnas virtuales
			*/
		};
	}
}

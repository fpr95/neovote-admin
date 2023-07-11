package com.digiteo.neovoteIV;

import com.digiteo.neovoteIV.model.jpa.data.Election;
import com.digiteo.neovoteIV.model.jpa.data.AdminEntity;
import com.digiteo.neovoteIV.model.jpa.data.VoterEntity;
import com.digiteo.neovoteIV.model.jpa.repository.ElectionRepository;
import com.digiteo.neovoteIV.model.jpa.repository.AdminRepository;
import com.digiteo.neovoteIV.model.jpa.repository.VoterRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@SpringBootApplication // (exclude = {SecurityAutoConfiguration.class})
public class NeovoteIvApplication {

	public static void main(String[] args) {
		SpringApplication.run(NeovoteIvApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(AdminRepository adminRepository,
										VoterRepository voterRepository,
										ElectionRepository electionRepository,
										PasswordEncoder encoder){
		return args -> {
			AdminEntity pepe = new AdminEntity(
					"Pepe",
					"Grillo",
					"admin",
					"admin@neovote.cl", // mail should be something like 'admin@neovote.cl'
			encoder.encode("Admin2023_"),
					"Hombre");

			pepe.setAccountVerified(true);
			adminRepository.save(pepe);

			VoterEntity jhon = new VoterEntity(
					"Jhon",
					"Doe",
					"jhon1",
					"fabo_willy_76@live.cl", // mail should be something like 'admin@neovote.cl'
					encoder.encode("Jhon2023_"),
					"Hombre");

			jhon.setAccountVerified(true);
			voterRepository.save(jhon);


			Election election = new Election(
					"¡Vota por tu favorito! Deporte: Talleres Formativos 2023",
					"Fútbol, Voleibol, Zumba ¡y más! Vota por el taller en el que te gustaría participar este 2023",
					"salud,entretencion",
					LocalDateTime.of(2023, 07, 20, 00, 00),
					LocalDateTime.of(2023, 07, 20, 23, 59),
					"Este 11 de Abril, durante todo el día, se abren las urnas para votar por el taller formativo en " +
							"el que TÚ desees participar. Contamos con profesores/as y preparadores físicos certificados en las siguientes áreas:\n" +
							"Voleibol, Fútbol, Basquetbol, Zumba y Boxeo\n" +
							"Las 3 opciones más votadas tendrán un espacio esta edición 2023 y comenzarán las clases el día 15");
			election.setCreatorUsername("jhon1");

			electionRepository.save(election);

		};
	}
}

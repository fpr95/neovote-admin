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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@SpringBootApplication // (exclude = {SecurityAutoConfiguration.class})
public class NeovoteIvApplication {

	public static void main(String[] args) {
		SpringApplication.run(NeovoteIvApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(UserRepository userRepository, ElectionRepository electionRepository, PasswordEncoder encoder){
		return args -> {
			UserEntity pepe = new UserEntity(
					"Pepe",
					"Grillo",
					"pepe123",
					"fabo_willy_76@live.cl",
			encoder.encode("Pepe2023_"),
					"Hombre");

			pepe.setAccountVerified(true);

			userRepository.save(pepe);


			Election election = new Election(
					"¡Vota por tu favorito! Deporte: Talleres Formativos 2023",
					"Fútbol, Voleibol, Zumba ¡y más! Vota por el taller en el que te gustaría participar este 2023",
					"salud,entretencion",
					LocalDateTime.of(2023, 04, 11, 00, 00),
					LocalDateTime.of(2023, 04, 11, 23, 59),
					"Este 11 de Abril, durante todo el día, se abren las urnas para votar por el taller formativo en " +
							"el que TÚ desees participar. Contamos con profesores/as y preparadores físicos certificados en las siguientes áreas:\n" +
							"Voleibol, Fútbol, Basquetbol, Zumba y Boxeo\n" +
							"Las 3 opciones más votadas tendrán un espacio esta edición 2023 y comenzarán las clases el día 15");
			election.setCreatorUsername("jhon1");

			electionRepository.save(election);

			/*
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

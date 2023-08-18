package com.digiteo.neovoteIV;

import com.digiteo.neovoteIV.model.jpa.data.*;
import com.digiteo.neovoteIV.model.jpa.repository.*;
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
										ProposalRepository proposalRepository,
										PasswordEncoder encoder){
		return args -> {
			// Super Admin account
			AdminEntity brian = new AdminEntity(
					"Brian",
					"Epstein",
					"admin",
					"admin@neovote.cl",
			encoder.encode("Admin2023."),
					"Custom");

			brian.setAccountVerified(true);
			adminRepository.save(brian);

			// here are 4 voters accounts
			VoterEntity jhon = new VoterEntity(
					"Jhon",
					"Lennon",
					"jhon1",
					"beatle_1@hotmail.com",
					encoder.encode("Jhon2023."),
					"Hombre");
			jhon.setAccountVerified(true);

			VoterEntity paul = new VoterEntity(
					"Paul",
					"McCartney",
					"paul1",
					"beatle_2@gmail.com",
					encoder.encode("Paul2023."),
					"Hombre");
			paul.setAccountVerified(true);

			VoterEntity george = new VoterEntity(
					"George",
					"Harrison",
					"george1",
					"beatle_3@live.cl",
					encoder.encode("George2023."),
					"Hombre");
			george.setAccountVerified(true);

			VoterEntity ringo = new VoterEntity(
					"Ringo",
					"Starr",
					"ringo1",
					"beatle_4@outlook.com",
					encoder.encode("Ringo2023."),
					"Hombre");
			ringo.setAccountVerified(true);

			voterRepository.save(jhon);
			voterRepository.save(paul);
			voterRepository.save(george);
			voterRepository.save(ringo);

			//----------------------------------------------------------------------------------------------------------
			// election 1 process for test purposes
			Election election = new Election(
					"Votación Dirigentes Sindicato de Trabajadores Mall",
					"Invitamos a nuestros colaboradores a votar por un nuevo/a directivo/a para nuestro sindicato",
					"administración,social",
					LocalDateTime.of(2023, 8, 18, 11, 58),
					LocalDateTime.of(2023, 8, 18, 12, 00),
					"Estimados socios sindicato Mall: Se informa que el proceso de votación\n" +
							"para la elección del(a) nuev@ dirigente sindical será este Lunes 24 de Julio del\n" +
							"presente año. Se solicita su participación para el proceso ya que hemos tomado\n" +
							"la decisión de agregar los votos no emitidos al candidato que tenga la mayor\n" +
							"preferencia al momento de finalizar la votación, por lo que rogamos la máxima\n" +
							"participación para esta convocatoria. Los resultados del proceso podrán ser vistos\n" +
							"aquí mismo, solo deberán ingresar con el nombre y la contraseña que les será enviada\n" +
							"a la brevedad. Consultas y dudas quedamos atentos.\n" +
							"Atte La Administración del Sindicato #14 de Trabajadores del Mall");
			election.setCreatorUsername("admin");
			electionRepository.save(election);

			// 4 proposals for the election 1 process
			Proposal proposal1 = new Proposal(
					"Angela Arancibia Vidal",
					"anarvi@hotmail.com",
					null,
					null,
					null,
					null,
					null,
					"El momento de actuar es ahora. De un tiempo a la fecha casi todos los miembros de este " +
							"sindicato hemos estado pidiendo soluciones para fallos recurrentes por parte de la administración actual.",
					true);
			proposal1.setBindingProcess(election);

			Proposal proposal2 = new Proposal(
					"Fernando Gonzales Díaz",
					"fer_gonzales@gmail.com",
					null,
					null,
					null,
					null,
					null,
					"El objetivo es claro: Mejorar aspectos como los bonos y respetar derechos tan básicos como " +
							"lo son las vacaciones para el personal de nuestra querida comunidad. Vota por Fernando si quieres aguinaldo.",
					true);
			proposal2.setBindingProcess(election);

			Proposal proposal3 = new Proposal(
					"Alejandra Ramirez Soto",
					"alejandraramirezsoto1@gmail.com",
					null,
					null,
					null,
					null,
					null,
					"La administración anterior no supo como solucionar los problemas que realmente aquejan a los" +
							" miembros de nuestro gremio, esta es la oportunidad para un cambio real y concreto, ¡Vota por mi!.",
					true);
			proposal3.setBindingProcess(election);

			Proposal proposal4 = new Proposal(
					"Isaac Cruz Fuentes",
					"isaac_rcf@gmail.com",
					null,
					null,
					null,
					null,
					null,
					"Hola, soy Isaac Cruz, tal vez me recuerdes por participar en el reality Necesito Atencion " +
							"VIP, bueno esta vez voy por la dirección del sindicato de trabajadores del centro comercial" +
							" de tu comuna para lograr el cambio, ¡el cambio verdadero!",
					true);
			proposal4.setBindingProcess(election);

			proposalRepository.save(proposal1);
			proposalRepository.save(proposal2);
			proposalRepository.save(proposal3);
			proposalRepository.save(proposal4);

			//----------------------------------------------------------------------------------------------------------
			// election 2 process for test purposes
			Election election2 = new Election(
					"¡Vota por tu favorito! Deporte: Talleres Formativos 2023",
					"Fútbol, Voleibol, Zumba ¡y más! Vota por el taller en el que te gustaría participar este 2023",
					"social, salud",
					LocalDateTime.of(2023, 8, 17, 17, 00),
					LocalDateTime.of(2023, 8, 17, 17, 59),
					"El Municipio Transformador de Villa Alemana presenta la edición 2023 de \"Talleres Formativos\", " +
							"esta vez enfocados en brindar un espacio de recreación saludable para todos y todas. Este " +
							"14 de Agosto, durante todo el día, se abren las urnas virtuales para votar por el taller " +
							"formativo en el que TÚ desees participar. Contamos con profesores/as y preparadores físicos " +
							"certificados en las siguientes áreas: Voleibol, Fútbol, Basquetbol, Zumba y Boxeo\n" +
							"Las 3 opciones más votadas tendrán un espacio en esta edición y comenzarán las clases el " +
							"día Sábado 26 del presente mes a las 10 de la mañana. ¡Te esperamos!");
			election2.setCreatorUsername("admin");
			electionRepository.save(election2);

			// 5 proposals for the election 2 process
			Proposal proposal1_e2 = new Proposal(
					"Taller de Voleibol",
					"oirs@villalemana.cl",
					"www.villalemana.cl",
					null,
					null,
					null,
					null,
					"¡El deporte ideal para jugar en la playa con amig@s y en campeonatos oficiales! Todo es posible" +
							" con una ex-campeona nacional como entrenadora, en Agosto contamos contigo.\n" +
							"¡Vota por nuestro taller!",
					true);

			proposal1_e2.setBindingProcess(election2);

			Proposal proposal2_e2 = new Proposal(
					"Taller de Fútbol",
					"oirs@villalemana.cl",
					"www.villalemana.cl",
					null,
					null,
					null,
					null,
					"Un clásico que nunca pasa de moda. En esta nueva edición niños, niñas y adult@s podrán venir " +
							"a participar en las canchas habilitadas dentro del polideportivo de la comuna en un ambiente grato y familiar\n" +
							"¡Vota por este taller!",
					true);

			proposal2_e2.setBindingProcess(election2);

			Proposal proposal3_e2 = new Proposal(
					"Taller de Basquetbol",
					"oirs@villalemana.cl",
					"www.villalemana.cl",
					null,
					null,
					null,
					null,
					"Representando el taller de basquetbol de esta edición 2023 tendremos al ex entrenador de " +
							"Los Leones de Quilpué con nosotros, aprovecha esta oportunidad con un grande a nivel " +
							"nacional y ¡vota por nuestro taller!",
					true);

			proposal3_e2.setBindingProcess(election2);

			Proposal proposal4_e2 = new Proposal(
					"Taller de Zumba",
					"oirs@villalemana.cl",
					"www.villalemana.cl",
					null,
					null,
					null,
					null,
					"Más que un tiempo de recreación al aire libre, la zumba nos permite trabajar nuestro sistema " +
							"cardiovascular como pocas otras actividades. Elige nuestro taller que será dirigido para un" +
							" público de todas las edades, ¡la diversión está garantizada!.",
					true);

			proposal4_e2.setBindingProcess(election2);

			Proposal proposal5_e2 = new Proposal(
					"Taller de Boxeo",
					"oirs@villalemana.cl",
					"www.villalemana.cl",
					null,
					null,
					null,
					null,
					"El boxeo es un deporte de contacto de alta exigencia física y mucha disciplina. Contaremos " +
							"con la guía de el team Nazareno quienes entrenaron al actual campeón sudamericano de la " +
							"WBC en peso welter. ¡Este taller cuenta con lo necesario para volverte el próximo Rocky!",
					true);

			proposal5_e2.setBindingProcess(election2);

			proposalRepository.save(proposal1_e2);
			proposalRepository.save(proposal2_e2);
			proposalRepository.save(proposal3_e2);
			proposalRepository.save(proposal4_e2);
			proposalRepository.save(proposal5_e2);
		};
	}
}

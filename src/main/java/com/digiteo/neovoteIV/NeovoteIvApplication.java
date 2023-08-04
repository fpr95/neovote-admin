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
										VoteRepository voteRepository,
										PasswordEncoder encoder){
		return args -> {
			AdminEntity brian = new AdminEntity(
					"Brian",
					"Epstein",
					"admin",
					"admin@neovote.cl", // mail should be something like 'admin@neovote.cl'
			encoder.encode("Admin2023_"),
					"Hombre");

			brian.setAccountVerified(true);
			adminRepository.save(brian);

			VoterEntity jhon = new VoterEntity(
					"Jhon",
					"Lennon",
					"jhon1",
					"fabo_willy_76@live.cl",
					encoder.encode("Jhon2023_"),
					"Hombre");

			jhon.setAccountVerified(true);

			VoterEntity paul = new VoterEntity(
					"Paul",
					"McCartney",
					"paul1",
					"fabo_willy_77@live.cl",
					encoder.encode("Paul2023_"),
					"Hombre");

			paul.setAccountVerified(true);

			VoterEntity george = new VoterEntity(
					"George",
					"Harrison",
					"george1",
					"fabo_willy_78@live.cl",
					encoder.encode("George2023_"),
					"Hombre");

			george.setAccountVerified(true);

			VoterEntity ringo = new VoterEntity(
					"Ringo",
					"Starr",
					"ringo1",
					"fabo_willy_79@live.cl",
					encoder.encode("Ringo2023_"),
					"Hombre");

			ringo.setAccountVerified(true);

			voterRepository.save(jhon);
			voterRepository.save(paul);
			voterRepository.save(george);
			voterRepository.save(ringo);

			Election election = new Election(
					"Votación Dirigentes Sindicato de Trabajadores Mall",
					"Invitamos a nuestros colaboradores a votar por un nuevo/a directivo/a para nuestro sindicato",
					"administración,social",
					LocalDateTime.of(2023, 8, 03, 17, 59),
					LocalDateTime.of(2023, 8, 03, 18, 00),
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

			Proposal proposal1 = new Proposal(
					"Angela Arancibia Vidal",
					"anarvi@hotmail.com",
					"El momento de actuar es ahora. De un tiempo a la fecha casi todos los miembros de este sindicato hemos estado pidiendo soluciones para fallos recurrentes por parte de la administración actual.",
					true);

			proposal1.setBindingProcess(election);

			Proposal proposal2 = new Proposal(
					"Fernando Gonzales Díaz",
					"fer_gonzales@gmail.com",
					"El objetivo es claro: Mejorar aspectos como los bonos y respetar derechos tan básicos como lo son las vacaciones para el personal de nuestra querida comunidad. Vota por Fernando si quieres aguinaldo.",
					true);

			proposal2.setBindingProcess(election);

			Proposal proposal3 = new Proposal(
					"Alejandra Ramirez Soto",
					"alejandraramirezsoto1@gmail.com",
					"La administración anterior no supo como solucionar los problemas que realmente aquejan a los miembros de nuestro gremio, esta es la oportunidad para un cambio real y concreto, ¡Vota por mi!.",
					true);

			proposal3.setBindingProcess(election);

			Proposal proposal4 = new Proposal(
					"Isaac Cruz Fuentes",
					"isaac_rcf@gmail.com",
					"Hola, soy Isaac Cruz, tal vez me recuerdes por participar en el reality Quiero Atencion Vip, bueno esta vez voy por la dirección del sindicato de trabajadores para lograr el cambio verdader, vota por mi!",
					true);

			proposal4.setBindingProcess(election);

			proposalRepository.save(proposal1);
			proposalRepository.save(proposal2);
			proposalRepository.save(proposal3);
			proposalRepository.save(proposal4);

			/*
			Vote v1 = new Vote();
			v1.setVoter(jhon);
			v1.setTargetProposal(proposal1);

			Vote v2 = new Vote();
			v1.setVoter(paul);
			v1.setTargetProposal(proposal2);

			Vote v3 = new Vote();
			v1.setVoter(ringo);
			v1.setTargetProposal(proposal1);

			Vote v4 = new Vote();
			v1.setVoter(george);
			v1.setTargetProposal(proposal3);

			voteRepository.save(v1);
			voteRepository.save(v2);
			voteRepository.save(v3);
			voteRepository.save(v4);
			*/
		};
	}
}

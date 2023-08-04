package com.digiteo.neovoteIV.model.service.impl;

import com.digiteo.neovoteIV.model.jpa.data.Proposal;
import com.digiteo.neovoteIV.model.jpa.data.Vote;
import com.digiteo.neovoteIV.model.jpa.data.VoterEntity;
import com.digiteo.neovoteIV.model.jpa.repository.ProposalRepository;
import com.digiteo.neovoteIV.model.jpa.repository.VoteRepository;
import com.digiteo.neovoteIV.model.jpa.repository.VoterRepository;
import com.digiteo.neovoteIV.model.service.VoteService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DefaultVoteService implements VoteService {

    private final VoteRepository voteRepository;
    private final VoterRepository voterRepository;
    private final ProposalRepository proposalRepository;

    @Override
    public void addVote(String proposalId, Principal principal) {
        Vote vote = new Vote();
        Optional<VoterEntity> voter = voterRepository.findVoterByUsernameOrEmail(principal.getName());
        if(voter.isPresent()){
            vote.setVoter(voter.get());
        } else {
            throw new EntityNotFoundException("votante no encontrado en la base de datos");
        }
        Optional<Proposal> proposal = proposalRepository.findProposalByName(proposalId);
        if(proposal.isPresent()){
            vote.setTargetProposal(proposal.get());
        } else {
            System.out.println(proposalId);
            throw new EntityNotFoundException("propuesta no encontrada en la base de datos");
        }
        voteRepository.save(vote);
        voter.get().getVotes().add(vote);
        proposal.get().getVotes().add(vote);
    }

    @Override
    public String[] findMostVoted(List<Proposal> proposals){
        int refValue = 0;
        Map<String, Integer> map = new HashMap<>();
        //System.out.println("|============= valores de la lista de propuestas pasadas como parámetro: =========================|");
        //proposals.forEach((pr) -> System.out.println(pr.getName()));
        for(Proposal p:proposals){
            String pName = p.getName();
            int votes = p.getVotes().size();
            map.put(pName, votes);
            //System.out.println("|============= valores agregados al map dentro del loop: =========================|");
            //map.forEach((k, v) -> System.out.println("Nombre Propuesta: " + k + ", N° Votos: " + v));
            if(votes > refValue){
                refValue = votes;
                //System.out.println("|============= refValue dentro del if dentro del loop: " + refValue + "=========================0|");
            }
        }
        Integer maxValue = refValue;
        //System.out.println("|=================== maxValue final: " + maxValue + "=====================================|");
        // recorrer map para ver que valores están incluidos hasta este punto, de esa forma se determinaría la causa
        // del problema en los métodos concatenados debajo
        //map.forEach((k, v) -> System.out.println("Key: " + k + ", Value: " + v));
        String[] winners = map.entrySet()
                .stream()
                .filter(f ->  f.getValue().equals(maxValue))
                .map(Map.Entry::getKey)
                .toArray(String[]::new);
        return winners;
    }

    @Override
    public int getParticipationPercent(Collection<VoterEntity> roll, Collection<Proposal> proposals){
        int totalParticipants = roll.size();
        int totalVotes = 0;
        for(Proposal p:proposals){
            int votes = p.getVotes().size();
            totalVotes = totalVotes + votes;
        }
        Double percent = (double)totalVotes*100/totalParticipants;
        return percent.intValue();
    }

}

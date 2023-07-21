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
import java.util.Optional;

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
}

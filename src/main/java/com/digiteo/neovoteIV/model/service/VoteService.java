package com.digiteo.neovoteIV.model.service;

import com.digiteo.neovoteIV.model.jpa.data.Proposal;
import com.digiteo.neovoteIV.model.jpa.data.VoterEntity;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

public interface VoteService {

        void addVote(String proposalId, Principal principal);
        String[] findMostVoted(List<Proposal> proposals);
        int getParticipationPercent(Collection<VoterEntity> roll, Collection<Proposal> proposals);
}

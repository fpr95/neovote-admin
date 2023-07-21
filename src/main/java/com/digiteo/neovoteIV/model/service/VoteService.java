package com.digiteo.neovoteIV.model.service;

import java.security.Principal;

public interface VoteService {

        void addVote(String proposalId, Principal principal);
}

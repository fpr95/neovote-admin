package com.digiteo.neovoteIV.model.service;

import com.digiteo.neovoteIV.model.jpa.data.Election;
import com.digiteo.neovoteIV.model.jpa.data.Proposal;
import com.digiteo.neovoteIV.system.exception.ProposalNameAlreadyExistException;
import com.digiteo.neovoteIV.web.data.model.ProposalData;
import com.digiteo.neovoteIV.web.data.model.ProposalUpdateData;

import java.util.List;

public interface ProposalService {

    void addProposal(ProposalData proposalData, Election election)
            throws ProposalNameAlreadyExistException;
    void checkIfProposalExist(String name)
        throws ProposalNameAlreadyExistException;
    void partialUpdate(String name, ProposalUpdateData pud)
            throws ProposalNameAlreadyExistException;
    void partialUpdatePlus(String name, ProposalUpdateData pud);
    void deleteProposal(String proposalName);
    List<ProposalData> getProposalList();
    public Proposal findProposal(String proposalName);
}

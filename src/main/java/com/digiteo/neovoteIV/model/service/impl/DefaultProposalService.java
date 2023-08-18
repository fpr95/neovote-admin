package com.digiteo.neovoteIV.model.service.impl;

import com.digiteo.neovoteIV.model.jpa.data.Election;
import com.digiteo.neovoteIV.model.jpa.data.Proposal;
import com.digiteo.neovoteIV.model.jpa.repository.ProposalRepository;
import com.digiteo.neovoteIV.model.mapper.ProposalMapper;
import com.digiteo.neovoteIV.model.service.ProposalService;
import com.digiteo.neovoteIV.system.exception.ProposalNameAlreadyExistException;
import com.digiteo.neovoteIV.web.data.model.ProposalData;
import com.digiteo.neovoteIV.web.data.model.ProposalUpdateData;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DefaultProposalService implements ProposalService {

    private final ProposalRepository repository;
    private final ProposalMapper mapper;

    @Override
    public void addProposal(ProposalData proposalData, Election election) throws ProposalNameAlreadyExistException {
        checkIfProposalExist(proposalData.getName());
        Proposal p = new Proposal();
        BeanUtils.copyProperties(proposalData, p);
        election.addProposal(p);
        repository.save(p);
    }

    @Override
    public void checkIfProposalExist(String name) throws ProposalNameAlreadyExistException {
        Optional<Proposal> p = repository.findProposalByName(name);
        if(p.isPresent()){
            throw new ProposalNameAlreadyExistException("Ya existe una propuesta con ese nombre, por favor ingrese otro");
        }
    }

    @Override
    @Transactional
    public void partialUpdate(String name, ProposalUpdateData pud) throws ProposalNameAlreadyExistException {
        //if(repository.findProposalByName(pud.getName()).isPresent()){
        //    throw new ProposalNameAlreadyExistException("Ya existe una propuesta con ese nombre, por favor ingrese otro");
        //}
        checkIfProposalExist(pud.getName());
        Proposal p = repository.findProposalByName(name).get();
        Election e = p.getBindingProcess();
        e.removeProposal(p);
        Proposal newProposal = mapper.patchToEntity(p, pud);
        Election newBindingProcess = p.getBindingProcess();
        newBindingProcess.addProposal(newProposal);
        repository.save(newProposal);
    }

    //------------------------------------------------------------------------------------------------------------------
    @Override
    @Transactional
    public void partialUpdatePlus(String name, ProposalUpdateData pud){
        Proposal newProposal = mapper.patchToEntity(repository.findProposalByName(name).get(), pud);
        repository.save(newProposal);
    }

    //------------------------------------------------------------------------------------------------------------------
    @Override
    public void deleteProposal(String proposalName) {
        Optional<Proposal> op = repository.findProposalByName(proposalName);
        if(op.isPresent()) {
            Proposal p = op.get();
            Election e = p.getBindingProcess();
            e.removeProposal(p);
            repository.delete(p);
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    @Override
    public List<ProposalData> getProposalList() {
        return null;
    }

    //------------------------------------------------------------------------------------------------------------------
    @Override
    public Proposal findProposal(String proposalName){
        Optional<Proposal> p = repository.findProposalByName(proposalName);
        if(p.isEmpty()){
            throw new EntityNotFoundException("No se encontró una propuesta con el nombre indicado");
        } else {
            return p.get();
        }
    }
}

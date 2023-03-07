package com.digiteo.neovoteIV.model.service.impl;

import com.digiteo.neovoteIV.model.jpa.data.Election;
import com.digiteo.neovoteIV.model.jpa.repository.ElectionRepository;
import com.digiteo.neovoteIV.model.service.ElectionService;
import com.digiteo.neovoteIV.system.exception.ElectionAlreadyExistException;
import com.digiteo.neovoteIV.web.data.model.ElectionData;
import com.digiteo.neovoteIV.web.data.model.ElectionListData;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class DefaultElectionService implements ElectionService {

    @Autowired // change this to constructor injection!!!
    ElectionRepository repository;

    @Override
    @Transactional
    public void addElection(ElectionData electionData, Principal principal) throws ElectionAlreadyExistException{
        checkIfElectionExist(electionData.getTitle());
        Election e = new Election();
        BeanUtils.copyProperties(electionData, e);
        e.setCreatorUsername(principal.getName());
        repository.save(e);
    }

    @Override
    public void checkIfElectionExist(String title) throws ElectionAlreadyExistException {
        Optional<Election> e = repository.findElectionByTitle(title);
        if(e.isPresent()){
            throw new ElectionAlreadyExistException("Ya existe una elección con este título, por favor escoga otro");
        }
    }

    @Override
    public void partialUpdate(Long id) {

    }

    @Override
    public void deleteElection(Long id) {
        Election e = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No existe una elección con este ID"));
        repository.delete(e);
    }


    // test this method on elections-list view
    @Override
    public List<ElectionListData> getElectionsList(){
        List<Election> elections = repository.findAll();
        // CHECK WTF RETURNS 'findAll()' IF THERE'S NO ENTITY SAVED IN THE DB.
        // MEANWHILE THE CONDITIONAL BELLOW WILL WORK WITH A null VALUE.
        if(elections == null){
            return Collections.emptyList();
        }
        List<ElectionListData> electionList = new ArrayList<>();
        for(Election e:elections){
            // 1. HERE SHOULD IMPLEMENT A SPLIT OVER THE 'topics' FIELD BEFORE MAPPING IT TO THE 'ElectionListData'
            //    WHEN THAT IS DONE CHANGE THE TYPE OF 'topics' ON THE TARGET FROM 'String' TO 'List<String>'
            // 2. ALSO NEED A WAY TO SET THE 'String' VALUE OF THE 'ElectionStatus' IN THE TARGET
            ElectionListData eld = new ElectionListData();
            BeanUtils.copyProperties(e, eld);
            eld.setStatus(e.getElectionStatusCode());
            eld.setTopics(e.getTopicsCollection());
            electionList.add(eld);
        }
        return electionList;
    }
}

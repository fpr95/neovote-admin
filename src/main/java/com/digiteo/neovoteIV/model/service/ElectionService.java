package com.digiteo.neovoteIV.model.service;

import com.digiteo.neovoteIV.system.exception.ElectionAlreadyExistException;
import com.digiteo.neovoteIV.web.data.model.ElectionData;
import com.digiteo.neovoteIV.web.data.model.ElectionListData;

import java.security.Principal;
import java.util.List;

public interface ElectionService {

    void addElection(ElectionData electionData, Principal principal)
            throws ElectionAlreadyExistException;
    void checkIfElectionExist(String title)
            throws ElectionAlreadyExistException;
    void partialUpdate(Long id);
    void deleteElection(Long id);
    List<ElectionListData> getElectionsList();
}

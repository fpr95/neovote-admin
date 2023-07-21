package com.digiteo.neovoteIV.model.service;

import com.digiteo.neovoteIV.model.jpa.data.Election;
import com.digiteo.neovoteIV.system.exception.ElectionAlreadyExistException;
import com.digiteo.neovoteIV.web.data.model.ElectionData;
import com.digiteo.neovoteIV.web.data.model.ElectionListData;
import com.digiteo.neovoteIV.web.data.model.ElectionUpdateData;

import java.security.Principal;
import java.util.List;

public interface ElectionService {

    void addElection(ElectionData electionData, Principal principal)
            throws ElectionAlreadyExistException;
    void checkIfElectionExist(String title)
            throws ElectionAlreadyExistException;
    Election getElectionByTitle(String title);
    String partialUpdate(Long id, ElectionUpdateData electionUpdateData)
            throws ElectionAlreadyExistException;
    //------------------------------------------------------------------------------------------------------------------
    public Election partialUpdatePlus(Long id, ElectionUpdateData electionUpdateData);
    //------------------------------------------------------------------------------------------------------------------
    void deleteElection(Long id);
    List<ElectionListData> getElectionsList();

    void suspendElection(Election e);

    void activateElection(Election e);

    // Voters layer related methods  -----------------------------------------------------------------------------------

    List<ElectionListData> getVotersElectionsList();
}

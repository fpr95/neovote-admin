package com.digiteo.neovoteIV.model.service;

import com.digiteo.neovoteIV.model.jpa.data.VoterEntity;
import com.digiteo.neovoteIV.system.exception.EmailAlreadyExistException;
import com.digiteo.neovoteIV.system.exception.InvalidTokenException;
import com.digiteo.neovoteIV.system.exception.UsernameAlreadyExistException;
import com.digiteo.neovoteIV.web.data.model.VoterData;

public interface VoterService {

    void register(final VoterData voter)
            throws UsernameAlreadyExistException, EmailAlreadyExistException;

    void checkIfVoterExist(final String username, final String email)
            throws UsernameAlreadyExistException, EmailAlreadyExistException;

    public void sendRegistrationConfirmationEmail(VoterEntity voter);

    VoterEntity getVoterByUsernameOrEmail(String username);

    boolean verifyVoter(String token) throws InvalidTokenException;
    void deleteVoter(String username);
}

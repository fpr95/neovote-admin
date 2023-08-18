package com.digiteo.neovoteIV.model.service;

import com.digiteo.neovoteIV.model.jpa.data.Election;
import com.digiteo.neovoteIV.system.exception.EmailAlreadyExistException;
import com.digiteo.neovoteIV.system.exception.UsernameAlreadyExistException;
import com.digiteo.neovoteIV.web.data.model.RollRegistrationData;

import java.security.Principal;

public interface ElectionRollService {

    boolean checkIfUserIsRegistered(String username, String email) throws EmailAlreadyExistException, UsernameAlreadyExistException;

    boolean checkIfUserIsRegisteredPlus(String username);

    String returnEmailIfUserIsRegistered(String userId);

    boolean addToRoll(RollRegistrationData rrd, Election election, Principal principal);

    void deleteUserInRoll(String username);
}

package com.digiteo.neovoteIV.model.service;

import com.digiteo.neovoteIV.model.jpa.data.AdminEntity;
import com.digiteo.neovoteIV.system.exception.EmailAlreadyExistException;
import com.digiteo.neovoteIV.system.exception.InvalidTokenException;
import com.digiteo.neovoteIV.system.exception.UnknownIdentifierException;
import com.digiteo.neovoteIV.system.exception.UsernameAlreadyExistException;
import com.digiteo.neovoteIV.web.data.model.AdminData;

public interface AdminService {

    void register(final AdminData user)
            throws UsernameAlreadyExistException, EmailAlreadyExistException;

    void checkIfUserExist(final String username, final String email)
            throws UsernameAlreadyExistException, EmailAlreadyExistException;

    public void sendRegistrationConfirmationEmail(AdminEntity user);

    AdminEntity getUserByUsernameOrEmail(String username); // throws UnknownIdentifierException;

    boolean verifyUser(String token) throws InvalidTokenException;
    /*
    void registerPlus(final AdminData user)
        throws UsernameAlreadyExistException;

    void checkIfUserExistPlus(final String username)
            throws UsernameAlreadyExistException;
     */

}

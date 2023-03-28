package com.digiteo.neovoteIV.model.service;

import com.digiteo.neovoteIV.model.jpa.data.UserEntity;
import com.digiteo.neovoteIV.system.exception.EmailAlreadyExistException;
import com.digiteo.neovoteIV.system.exception.InvalidTokenException;
import com.digiteo.neovoteIV.system.exception.UnknownIdentifierException;
import com.digiteo.neovoteIV.system.exception.UsernameAlreadyExistException;
import com.digiteo.neovoteIV.web.data.model.UserData;

public interface UserService {

    void register(final UserData user)
            throws UsernameAlreadyExistException, EmailAlreadyExistException;

    void checkIfUserExist(final String username, final String email)
            throws UsernameAlreadyExistException, EmailAlreadyExistException;

    public void sendRegistrationConfirmationEmail(UserEntity user);

    UserEntity getUserByUsernameOrEmail(String username); // throws UnknownIdentifierException;

    boolean verifyUser(String token) throws InvalidTokenException;
    /*
    void registerPlus(final UserData user)
        throws UsernameAlreadyExistException;

    void checkIfUserExistPlus(final String username)
            throws UsernameAlreadyExistException;
     */

}

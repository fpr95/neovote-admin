package com.digiteo.neovoteIV.model.service;

import com.digiteo.neovoteIV.model.jpa.data.UserEntity;
import com.digiteo.neovoteIV.system.exception.EmailAlreadyExistException;
import com.digiteo.neovoteIV.system.exception.UsernameAlreadyExistException;
import com.digiteo.neovoteIV.web.data.model.UserData;

public interface UserService {

    void register(final UserData user)
            throws UsernameAlreadyExistException, EmailAlreadyExistException;

    void checkIfUserExist(final String username, final String email)
            throws UsernameAlreadyExistException, EmailAlreadyExistException;

    UserEntity getUserByUsernameOrEmail(String username);
    /*
    void registerPlus(final UserData user)
        throws UsernameAlreadyExistException;

    void checkIfUserExistPlus(final String username)
            throws UsernameAlreadyExistException;
     */

}

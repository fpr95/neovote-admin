package com.digiteo.neovoteIV.model.service;

import com.digiteo.neovoteIV.system.exception.InvalidTokenException;
import com.digiteo.neovoteIV.system.exception.UnknownIdentifierException;

public interface UserAccountService {
    void forgottenPassword(String email) throws UnknownIdentifierException;

    void updatePassword(String password, String token) throws InvalidTokenException, UnknownIdentifierException;

    boolean verifyResetPasswordToken(String token) throws InvalidTokenException;
}

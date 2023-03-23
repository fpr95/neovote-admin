package com.digiteo.neovoteIV.security.token;

import com.digiteo.neovoteIV.security.jpa.SecureToken;

public interface SecureTokenService {

    SecureToken createSecureToken();

    void saveSecureToken(SecureToken secureToken);
    SecureToken findByToken(String token);
    void removeToken(SecureToken token);

    void removeTokenByToken(String token);
}

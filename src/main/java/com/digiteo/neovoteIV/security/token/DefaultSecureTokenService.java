package com.digiteo.neovoteIV.security.token;

import com.digiteo.neovoteIV.security.jpa.SecureToken;
import com.digiteo.neovoteIV.security.token.repository.SecureTokenRepository;
import lombok.Getter;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.time.LocalDateTime;

@Getter
@Service
public class DefaultSecureTokenService implements SecureTokenService {

    // this is a simple impl for a token, if more security is required could be switched to a keypair or something like that
    private static final BytesKeyGenerator TOKEN_GENERATOR = KeyGenerators.secureRandom(16);
    //check if this charset works
    private static final Charset US_ASCII = Charset.forName("US-ASCII");

    @Value("${com.digiteo.secure.token.expSeconds}")
    private int tokenExpirationTime;

    @Autowired
    SecureTokenRepository repository;

    @Override
    public SecureToken createSecureToken() {
        String token = new String(Base64.encodeBase64URLSafeString(TOKEN_GENERATOR.generateKey() ));
        SecureToken secureToken = new SecureToken();
        secureToken.setToken(token);
        secureToken.setExpireAt(LocalDateTime.now().plusSeconds(getTokenExpirationTime()));
        this.saveSecureToken(secureToken);
        return secureToken;
    }

    //in case of need a secure token for other purposes
    @Override
    public void saveSecureToken(SecureToken secureToken) {
        repository.save(secureToken);
    }

    @Override
    public SecureToken findByToken(String token) {
        return repository.findByToken(token);
    }

    @Override
    public void removeToken(SecureToken token) {
        repository.delete(token);
    }

    @Override
    public void removeTokenByToken(String token) { repository.removeByToken(token); }
}

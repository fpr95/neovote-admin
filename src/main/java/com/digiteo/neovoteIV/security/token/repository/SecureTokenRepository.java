package com.digiteo.neovoteIV.security.token.repository;

import com.digiteo.neovoteIV.security.jpa.SecureToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecureTokenRepository extends JpaRepository<SecureToken, Long> {

    // this is to fetch and verify the info in the token
    SecureToken findByToken(final String token);
    // in case the token is used this method will be called to remove the token from the system
    Long removeByToken(String token);
}

package com.digiteo.neovoteIV.model.service.impl;

import com.digiteo.neovoteIV.email.EmailService;
import com.digiteo.neovoteIV.model.jpa.data.UserEntity;
import com.digiteo.neovoteIV.model.jpa.repository.UserRepository;
import com.digiteo.neovoteIV.security.token.repository.SecureTokenRepository;
import com.digiteo.neovoteIV.security.token.SecureTokenService;
import com.digiteo.neovoteIV.model.service.UserAccountService;
import com.digiteo.neovoteIV.model.service.UserService;
import com.digiteo.neovoteIV.email.context.ForgotPasswordEmailContext;
import com.digiteo.neovoteIV.security.jpa.SecureToken;
import com.digiteo.neovoteIV.system.exception.InvalidTokenException;
import com.digiteo.neovoteIV.system.exception.UnknownIdentifierException;
import jakarta.mail.MessagingException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class DefaultUserAccountService implements UserAccountService {

    private UserService userService;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private SecureTokenService secureTokenService;
    private SecureTokenRepository secureTokenRepository;
    private EmailService emailService;
    @Value("${site.base.url.http}")
    private String baseURL;

    @Autowired
    public DefaultUserAccountService(
            UserService userService,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            SecureTokenService secureTokenService,
            SecureTokenRepository secureTokenRepository,
            EmailService emailService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.secureTokenService = secureTokenService;
        this.secureTokenRepository = secureTokenRepository;
        this.emailService = emailService;
    }

    @Override
    public void forgottenPassword(String email) { //throws UnknownIdentifierException {
        UserEntity user = userService.getUserByUsernameOrEmail(email);
        sendResetPassword(user);
    }

    //here there's a chance to send a new email notifying the user that the account(the password) has been modified
    @Override
    public void updatePassword(String password, String token) throws InvalidTokenException, UnknownIdentifierException {
        SecureToken secureToken = secureTokenService.findByToken(token);
        if(Objects.isNull(secureToken) || !StringUtils.equals(token, secureToken.getToken()) || secureToken.isExpired()){
            throw new InvalidTokenException("El token proporcionado no es válido");
        }
        UserEntity user = userRepository.getReferenceById(secureToken.getUserEntity().getId());
        if(Objects.isNull(user)){
            throw new UnknownIdentifierException("Error al buscar al usuario asociado al token proporcionado");
        }
        secureTokenService.removeToken(secureToken);
        user.setPwd(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    protected void sendResetPassword(UserEntity user) {
        SecureToken secureToken = secureTokenService.createSecureToken();
        secureToken.setUserEntity(user);
        secureTokenRepository.save(secureToken);
        ForgotPasswordEmailContext emailContext = new ForgotPasswordEmailContext();
        emailContext.init(user);
        emailContext.setToken(secureToken.getToken());
        emailContext.buildVerificationUrl(baseURL, secureToken.getToken());
        try {
            emailService.sendEmail(emailContext);
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean verifyResetPasswordToken(String token) throws InvalidTokenException{
        SecureToken secureToken = secureTokenService.findByToken(token);
        if(Objects.isNull(secureToken) || !StringUtils.equals(token, secureToken.getToken()) || secureToken.isExpired()){
            throw new InvalidTokenException("El token proporcionado no es válido");
        }
        return true;
    }

}

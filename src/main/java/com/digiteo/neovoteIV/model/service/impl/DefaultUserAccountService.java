package com.digiteo.neovoteIV.model.service.impl;

import com.digiteo.neovoteIV.email.EmailService;
import com.digiteo.neovoteIV.model.jpa.data.AdminEntity;
import com.digiteo.neovoteIV.model.jpa.data.VoterEntity;
import com.digiteo.neovoteIV.model.jpa.repository.AdminRepository;
import com.digiteo.neovoteIV.model.jpa.repository.VoterRepository;
import com.digiteo.neovoteIV.model.service.VoterService;
import com.digiteo.neovoteIV.security.token.repository.SecureTokenRepository;
import com.digiteo.neovoteIV.security.token.SecureTokenService;
import com.digiteo.neovoteIV.model.service.UserAccountService;
import com.digiteo.neovoteIV.model.service.AdminService;
import com.digiteo.neovoteIV.email.context.ForgotPasswordEmailContext;
import com.digiteo.neovoteIV.security.jpa.SecureToken;
import com.digiteo.neovoteIV.system.exception.InvalidTokenException;
import com.digiteo.neovoteIV.system.exception.UnknownIdentifierException;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class DefaultUserAccountService implements UserAccountService {

    private AdminService adminService;
    private VoterService voterService;
    private AdminRepository adminRepository;
    private VoterRepository voterRepository;
    private PasswordEncoder passwordEncoder;
    private SecureTokenService secureTokenService;
    private SecureTokenRepository secureTokenRepository;
    private EmailService emailService;
    @Value("${site.base.url.http}")
    private String baseURL;

    @Autowired
    public DefaultUserAccountService(
            AdminService adminService,
            AdminRepository adminRepository,
            PasswordEncoder passwordEncoder,
            SecureTokenService secureTokenService,
            SecureTokenRepository secureTokenRepository,
            EmailService emailService) {
        this.adminService = adminService;
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.secureTokenService = secureTokenService;
        this.secureTokenRepository = secureTokenRepository;
        this.emailService = emailService;
    }

    @Override
    public void forgottenPassword(String email) { //throws UnknownIdentifierException {
        if(!adminService.getUserByUsernameOrEmail(email).getClass().equals(AdminEntity.class)){
            VoterEntity voter = voterService.getVoterByUsernameOrEmail(email);
            sendResetPasswordVoter(voter);
        } else {
            AdminEntity user = adminService.getUserByUsernameOrEmail(email);
            sendResetPassword(user);
        }
    }

    //here's a chance to send a new email notifying the user that the account(the password) has been modified
    @Override
    public void updatePassword(String password, String token) throws InvalidTokenException, UnknownIdentifierException {
        SecureToken secureToken = secureTokenService.findByToken(token);
        if(Objects.isNull(secureToken) || !StringUtils.equals(token, secureToken.getToken()) || secureToken.isExpired()){
            throw new InvalidTokenException("El token proporcionado no es válido");
        }
        if(!secureToken.getAdminEntity().getClass().equals(AdminEntity.class)){
            VoterEntity voter = voterRepository.getReferenceById(secureToken.getVoterEntity().getId());
            if(Objects.isNull(voter)){
                throw new UnknownIdentifierException("Error al buscar al votante asociado al token proporcionado");
            }
            secureTokenService.removeToken(secureToken);
            voter.setPwd(passwordEncoder.encode(password));
            voterRepository.save(voter);
        } else { // test if this works within an if-else block
            AdminEntity user = adminRepository.getReferenceById(secureToken.getAdminEntity().getId());
            if (Objects.isNull(user)) {
                throw new UnknownIdentifierException("Error al buscar al usuario asociado al token proporcionado");
            }
            secureTokenService.removeToken(secureToken);
            user.setPwd(passwordEncoder.encode(password));
            adminRepository.save(user);
        }
    }

    /*
    @Override
    public void updatePasswordVoter(String password, String token) throws InvalidTokenException, UnknownIdentifierException {
        SecureToken secureToken = secureTokenService.findByToken(token);
        if(Objects.isNull(secureToken) || !StringUtils.equals(token, secureToken.getToken()) || secureToken.isExpired()){
            throw new InvalidTokenException("El token proporcionado no es válido");
        }
        VoterEntity voter = voterRepository.getReferenceById(secureToken.getVoterEntity().getId());
        if(Objects.isNull(voter)){
            throw new UnknownIdentifierException("Error al buscar al votante asociado al token proporcionado");
        }
        secureTokenService.removeToken(secureToken);
        voter.setPwd(passwordEncoder.encode(password));
        voterRepository.save(voter);
    }
     */

    protected void sendResetPassword(AdminEntity user) {
        SecureToken secureToken = secureTokenService.createSecureToken();
        secureToken.setAdminEntity(user);
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

    protected void sendResetPasswordVoter(VoterEntity voter) {
        SecureToken secureToken = secureTokenService.createSecureToken();
        secureToken.setVoterEntity(voter);
        secureTokenRepository.save(secureToken);
        ForgotPasswordEmailContext emailContext = new ForgotPasswordEmailContext();
        emailContext.initVoter(voter);
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

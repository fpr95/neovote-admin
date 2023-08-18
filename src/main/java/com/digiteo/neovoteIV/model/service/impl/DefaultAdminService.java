package com.digiteo.neovoteIV.model.service.impl;

import com.digiteo.neovoteIV.email.EmailService;
import com.digiteo.neovoteIV.email.context.AccountVerificationEmailContext;
import com.digiteo.neovoteIV.model.jpa.data.AdminEntity;
import com.digiteo.neovoteIV.model.jpa.repository.AdminRepository;
import com.digiteo.neovoteIV.model.service.AdminService;
import com.digiteo.neovoteIV.security.jpa.SecureToken;
import com.digiteo.neovoteIV.security.token.SecureTokenService;
import com.digiteo.neovoteIV.security.token.repository.SecureTokenRepository;
import com.digiteo.neovoteIV.system.exception.EmailAlreadyExistException;
import com.digiteo.neovoteIV.system.exception.InvalidTokenException;
import com.digiteo.neovoteIV.system.exception.UsernameAlreadyExistException;
import com.digiteo.neovoteIV.web.data.model.AdminData;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Objects;
import java.util.Optional;

@Service("adminService")
@AllArgsConstructor
public class DefaultAdminService implements AdminService {

    private final AdminRepository adminRepository;
    private PasswordEncoder passwordEncoder;
    private SecureTokenService secureTokenService;
    private SecureTokenRepository secureTokenRepository;
    private EmailService emailService;
    //when implement https change for ".url.https"
    @Value("${site.base.url.http}")
    private String baseURL;

    @Autowired
    public DefaultAdminService(AdminRepository adminRepository,
                               PasswordEncoder passwordEncoder,
                               SecureTokenService secureTokenService,
                               SecureTokenRepository secureTokenRepository,
                               EmailService emailService){
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.secureTokenService = secureTokenService;
        this.secureTokenRepository = secureTokenRepository;
        this.emailService = emailService;
    }

    @Override
    @Transactional
    public void register(AdminData user) throws UsernameAlreadyExistException, EmailAlreadyExistException{
        checkIfUserExist(user.getUsername(), user.getEmail());
        AdminEntity adminEntity = new AdminEntity();
        BeanUtils.copyProperties(user, adminEntity);
        encodePwd(user, adminEntity);
        adminRepository.save(adminEntity);
        sendRegistrationConfirmationEmail(adminEntity);
    }

    @Override
    public void checkIfUserExist(String username, String email) throws UsernameAlreadyExistException, EmailAlreadyExistException {
        Optional<AdminEntity> u = adminRepository.findUserByUsername(username);
        if(u.isPresent()){
            throw new UsernameAlreadyExistException("¡Ya existe un administrador registrado con estas credenciales!(username)");
        }
        u = adminRepository.findUserByEmail(email);
        if(u.isPresent()) {
            throw new EmailAlreadyExistException("¡Ya existe un administrador registrado con estas credenciales!(email)");
        }
    }

    @Override
    public void sendRegistrationConfirmationEmail(AdminEntity user) {
        SecureToken secureToken = secureTokenService.createSecureToken();
        secureToken.setAdminEntity(user);
        secureTokenRepository.save(secureToken);
        AccountVerificationEmailContext emailContext = new AccountVerificationEmailContext();
        emailContext.init(user);
        emailContext.setToken(secureToken.getToken());
        emailContext.buildVerificationUrl(baseURL, secureToken.getToken());
        try {
            emailService.sendEmail(emailContext);
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
    }

    private void encodePwd(AdminData source, AdminEntity target) {
        target.setPwd(passwordEncoder.encode(source.getPwd()));
    }

    @Override
    public AdminEntity getUserByUsernameOrEmail(String username) { //throws UnknownIdentifierException {
        AdminEntity u = adminRepository.findUserByUsernameOrEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("No existe un usuario para el identifiador proporcionado."));
        //.orElseThrow(() -> new UnknownIdentifierException("No existe un usuario para el identifiador proporcionado."));
        return u;
    }

    @Override
    public boolean verifyUser(String token) throws InvalidTokenException {
        SecureToken secureToken = secureTokenService.findByToken(token);
        if(Objects.isNull(secureToken) || !StringUtils.equals(token, secureToken.getToken()) || secureToken.isExpired()){
            throw new InvalidTokenException("El token proporcionado no es válido");
        }
        AdminEntity user = adminRepository.getReferenceById(secureToken.getAdminEntity().getId());
        if(Objects.isNull(user)){
            return false;
        }
        user.setAccountVerified(true);
        adminRepository.save(user);

        secureTokenService.removeToken(secureToken);
        return true;
    }

    /*
    if this method works, eliminate 'UsernameAlreadyExistException' and 'EmailAlreadyExistException' and create a 'UserAlreadyExistException'.
    UPDATE: the 'register' method works better so this one it'll be commented along with all the methods related to it
    @Override
    @Transactional
    public void registerPlus(AdminData user) throws UsernameAlreadyExistException{
        checkIfUserExistPlus(user.getUsername());
        checkIfUserExistPlus(user.getEmail());
        AdminEntity adminEntity = new AdminEntity();
        BeanUtils.copyProperties(user, adminEntity);
        encodePwd(user, adminEntity);
        adminRepository.save(adminEntity);
    }

    public void checkIfUserExistPlus(String username) throws UsernameAlreadyExistException {
        Optional<AdminEntity> u = adminRepository.findUserByUsernameOrEmail(username);
        if(u.isPresent()){
            throw new UsernameAlreadyExistException("Ya existe un usuario con estas credenciales");
        }
    }
     */

    @Override
    public boolean authenticateAdminPassword(Principal principal, String pwd){
        boolean isCorrect = false;
        Optional<AdminEntity> ao = adminRepository.findUserByUsernameOrEmail(principal.getName());
        if(ao.isPresent()){
            if(passwordEncoder.matches(pwd, ao.get().getPwd())){
                isCorrect = true;
            }
        }
        return isCorrect;
    }

    @Override
    public void deleteAdminProfile(String username) throws Exception {
        if(!Objects.equals(username, "admin")){
            AdminEntity a = getUserByUsernameOrEmail(username);
            adminRepository.delete(a);
        } else {
            throw new Exception("|==================================| LA CUENTA DEL SUPER ADMINISTRADOR NO PUEDE " +
                    "SER ELIMINADA DEL SISTEMA |==================================|");
        }
    }

}

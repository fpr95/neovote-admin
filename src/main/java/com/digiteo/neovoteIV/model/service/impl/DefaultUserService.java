package com.digiteo.neovoteIV.model.service.impl;

import com.digiteo.neovoteIV.email.EmailService;
import com.digiteo.neovoteIV.email.context.AccountVerificationEmailContext;
import com.digiteo.neovoteIV.model.jpa.data.UserEntity;
import com.digiteo.neovoteIV.model.jpa.repository.UserRepository;
import com.digiteo.neovoteIV.model.service.UserService;
import com.digiteo.neovoteIV.security.jpa.SecureToken;
import com.digiteo.neovoteIV.security.token.SecureTokenService;
import com.digiteo.neovoteIV.security.token.repository.SecureTokenRepository;
import com.digiteo.neovoteIV.system.exception.EmailAlreadyExistException;
import com.digiteo.neovoteIV.system.exception.InvalidTokenException;
import com.digiteo.neovoteIV.system.exception.UsernameAlreadyExistException;
import com.digiteo.neovoteIV.web.data.model.UserData;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service("userService")
@AllArgsConstructor // check if this autowire the fields, if not,
// try 'onConstructor' attribute, if still not work,
// create custom constructor with @Autowired annotation
public class DefaultUserService implements UserService {

    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private SecureTokenService secureTokenService;
    private SecureTokenRepository secureTokenRepository;
    private EmailService emailService;
    //in case of implement https change for ".url.https"
    @Value("${site.base.url.http}")
    private String baseURL;

    @Autowired
    public DefaultUserService(UserRepository userRepository,
                              PasswordEncoder passwordEncoder,
                              SecureTokenService secureTokenService,
                              SecureTokenRepository secureTokenRepository,
                              EmailService emailService){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.secureTokenService = secureTokenService;
        this.secureTokenRepository = secureTokenRepository;
        this.emailService = emailService;
    }

    @Override
    @Transactional
    public void register(UserData user) throws UsernameAlreadyExistException, EmailAlreadyExistException{
        checkIfUserExist(user.getUsername(), user.getEmail());
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);
        //userEntity.setFirstName(user.getFirstName());
        //userEntity.setLastName(user.getLastName());
        //userEntity.setUsername(user.getUsername());
        //userEntity.setEmail(user.getEmail());
        encodePwd(user, userEntity);
        //userEntity.setGender(user.getGender());
        userRepository.save(userEntity);
        sendRegistrationConfirmationEmail(userEntity);
    }

    @Override
    public void checkIfUserExist(String username, String email) throws UsernameAlreadyExistException, EmailAlreadyExistException {
        Optional<UserEntity> u = userRepository.findUserByUsername(username);
        if(u.isPresent()){
            throw new UsernameAlreadyExistException("¡Ya existe un usuario registrado con estas credenciales!(username)");
        }
        u = userRepository.findUserByEmail(email);
        if(u.isPresent()) {
            throw new EmailAlreadyExistException("¡Ya existe un usuario registrado con estas credenciales!(email)");
        }
    }

    @Override
    public void sendRegistrationConfirmationEmail(UserEntity user) {
        SecureToken secureToken = secureTokenService.createSecureToken();
        secureToken.setUserEntity(user);
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

    private void encodePwd(UserData source, UserEntity target) {
        target.setPwd(passwordEncoder.encode(source.getPwd()));
    }

    @Override
    public UserEntity getUserByUsernameOrEmail(String username){
        UserEntity u = userRepository.findUserByUsernameOrEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("No existe un usuario para el identifiador proporcionado."));
        return u;
    }

    @Override
    public boolean verifyUser(String token) throws InvalidTokenException {
        SecureToken secureToken = secureTokenService.findByToken(token);
        if(Objects.isNull(secureToken) || !StringUtils.equals(token, secureToken.getToken()) || secureToken.isExpired()){
            throw new InvalidTokenException("El token proporcionado no es válido");
        }
        UserEntity user = userRepository.getReferenceById(secureToken.getUserEntity().getId());
        if(Objects.isNull(user)){
            return false;
        }
        user.setAccountVerified(true);
        userRepository.save(user);

        secureTokenService.removeToken(secureToken);
        return true;
    }

    /*
    if this method works, eliminate 'UsernameAlreadyExistException' and 'EmailAlreadyExistException' and create a 'UserAlreadyExistException'.
    UPDATE: In terms of UX the 'register' method works better so this one it'll be commented along with all the methods related to it
    @Override
    @Transactional
    public void registerPlus(UserData user) throws UsernameAlreadyExistException{
        checkIfUserExistPlus(user.getUsername());
        checkIfUserExistPlus(user.getEmail());
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);
        encodePwd(user, userEntity);
        userRepository.save(userEntity);
    }

    public void checkIfUserExistPlus(String username) throws UsernameAlreadyExistException {
        Optional<UserEntity> u = userRepository.findUserByUsernameOrEmail(username);
        if(u.isPresent()){
            throw new UsernameAlreadyExistException("Ya existe un usuario con estas credenciales");
        }
    }
     */

}

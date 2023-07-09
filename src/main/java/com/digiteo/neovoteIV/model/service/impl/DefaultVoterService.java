package com.digiteo.neovoteIV.model.service.impl;

import com.digiteo.neovoteIV.email.EmailService;
import com.digiteo.neovoteIV.email.context.AccountVerificationEmailContext;
import com.digiteo.neovoteIV.model.jpa.data.VoterEntity;
import com.digiteo.neovoteIV.model.jpa.repository.VoterRepository;
import com.digiteo.neovoteIV.model.service.VoterService;
import com.digiteo.neovoteIV.security.jpa.SecureToken;
import com.digiteo.neovoteIV.security.token.SecureTokenService;
import com.digiteo.neovoteIV.security.token.repository.SecureTokenRepository;
import com.digiteo.neovoteIV.system.exception.EmailAlreadyExistException;
import com.digiteo.neovoteIV.system.exception.InvalidTokenException;
import com.digiteo.neovoteIV.system.exception.UsernameAlreadyExistException;
import com.digiteo.neovoteIV.web.data.model.VoterData;
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

@Service("voterService")
@AllArgsConstructor
public class DefaultVoterService implements VoterService {

    private final VoterRepository voterRepository;
    private PasswordEncoder passwordEncoder;
    private SecureTokenService secureTokenService;
    private SecureTokenRepository secureTokenRepository;
    private EmailService emailService;
    //when implement https change for ".url.https"
    @Value("${site.base.url.http}")
    private String baseURL;

    @Autowired
    public DefaultVoterService(VoterRepository voterRepository,
                               PasswordEncoder passwordEncoder,
                               SecureTokenService secureTokenService,
                               SecureTokenRepository secureTokenRepository,
                               EmailService emailService) {
        this.voterRepository = voterRepository;
        this.passwordEncoder = passwordEncoder;
        this.secureTokenService = secureTokenService;
        this.secureTokenRepository = secureTokenRepository;
        this.emailService = emailService;
    }

    @Override
    @Transactional
    public void register(VoterData voter)throws UsernameAlreadyExistException, EmailAlreadyExistException {
        checkIfVoterExist(voter.getUsername(), voter.getEmail());
        VoterEntity voterEntity = new VoterEntity();
        BeanUtils.copyProperties(voter, voterEntity);
        encodePwd(voter, voterEntity);
        voterRepository.save(voterEntity);
        sendRegistrationConfirmationEmail(voterEntity);
    }

    @Override
    public void checkIfVoterExist(String username, String email) throws UsernameAlreadyExistException, EmailAlreadyExistException {
        Optional<VoterEntity> u = voterRepository.findVoterByUsername(username);
        if(u.isPresent()){
            throw new UsernameAlreadyExistException("¡Ya existe un votante registrado con estas credenciales!(username)");
        }
        u = voterRepository.findVoterByEmail(email);
        if(u.isPresent()) {
            throw new EmailAlreadyExistException("¡Ya existe un votante registrado con estas credenciales!(email)");
        }
    }

    private void encodePwd(VoterData source, VoterEntity target){
        target.setPwd(passwordEncoder.encode(source.getPwd()));
    }

    @Override
    public void sendRegistrationConfirmationEmail(VoterEntity voter) {
        SecureToken secureToken = secureTokenService.createSecureToken();
        secureToken.setVoterEntity(voter);
        secureTokenRepository.save(secureToken);
        AccountVerificationEmailContext emailContext = new AccountVerificationEmailContext();
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
    public VoterEntity getVoterByUsernameOrEmail(String username) {
        VoterEntity u = voterRepository.findVoterByUsernameOrEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("No existe un votante para el identifiador proporcionado."));
        return u;
    }

    @Override
    public boolean verifyVoter(String token) throws InvalidTokenException {
        SecureToken secureToken = secureTokenService.findByToken(token);
        if(Objects.isNull(secureToken) || !StringUtils.equals(token, secureToken.getToken()) || secureToken.isExpired()){
            throw new InvalidTokenException("El token proporcionado no es válido");
        }
        VoterEntity voter = voterRepository.getReferenceById(secureToken.getVoterEntity().getId());
        if(Objects.isNull(voter)){
            return false;
        }
        voter.setAccountVerified(true);
        voterRepository.save(voter);

        secureTokenService.removeToken(secureToken);
        return true;
    }
}

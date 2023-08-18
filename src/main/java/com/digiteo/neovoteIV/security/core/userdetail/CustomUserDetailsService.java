package com.digiteo.neovoteIV.security.core.userdetail;

import com.digiteo.neovoteIV.model.jpa.data.AdminEntity;
import com.digiteo.neovoteIV.model.jpa.data.VoterEntity;
import com.digiteo.neovoteIV.model.jpa.repository.AdminRepository;
import com.digiteo.neovoteIV.model.jpa.repository.VoterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    AdminRepository adminRepository;
    VoterRepository voterRepository;

    @Autowired
    public CustomUserDetailsService(AdminRepository adminRepository, VoterRepository voterRepository) {
        this.adminRepository = adminRepository;
        this.voterRepository = voterRepository;
    }

    /*
    @Override
    @Transactional  <- keep this with the "//"
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        Optional<AdminEntity> userOptional = adminRepository.findUserByUsernameOrEmail(usernameOrEmail);
        if(userOptional.isEmpty()){
            throw new UsernameNotFoundException("La credencial proporcionada no existe en el sistema");
        }
        boolean enabled = !userOptional.get().isAccountVerified();
        UserDetails user = User.withUsername(userOptional.get().getUsername())
                .password(userOptional.get().getPwd())
                .disabled(enabled)
                .authorities("USER")
                .build();
        return user;
    }
     */

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        /*
        Optional<AdminEntity> userOptional = adminRepository.findUserByUsernameOrEmail(usernameOrEmail);
        Optional<VoterEntity> voterOptional = voterRepository.findVoterByUsernameOrEmail(usernameOrEmail);
        if(voterOptional.isEmpty()){
            throw new UsernameNotFoundException("La credencial proporcionada no existe en el sistema");
        }
        if(!userOptional.get().getClass().equals(AdminEntity.class) || userOptional.get() == null){
        }
         */
        if(!adminRepository.findUserByUsernameOrEmail(usernameOrEmail).isEmpty()
                && adminRepository.findUserByUsernameOrEmail(usernameOrEmail).get() instanceof AdminEntity){
            Optional<AdminEntity> adminOptional = adminRepository.findUserByUsernameOrEmail(usernameOrEmail);
            boolean enabled = !adminOptional.get().isAccountVerified();
            UserDetails admin = User.withUsername(adminOptional.get().getUsername())
                    .password(adminOptional.get().getPwd())
                    .disabled(enabled)
                    .authorities("ADMIN")
                    .build();
            return admin;
        } else if (!voterRepository.findVoterByUsernameOrEmail(usernameOrEmail).isEmpty()
                && voterRepository.findVoterByUsernameOrEmail(usernameOrEmail).get() instanceof VoterEntity) {
            Optional<VoterEntity> voterOptional = voterRepository.findVoterByUsernameOrEmail(usernameOrEmail);
            boolean enabled = !voterOptional.get().isAccountVerified();
            UserDetails voter = User.withUsername(voterOptional.get().getUsername())
                    .password(voterOptional.get().getPwd())
                    .disabled(enabled)
                    .authorities("USER")
                    .build();
            return voter;
        } else {
            throw new UsernameNotFoundException("La credencial proporcionada no existe en el sistema");
        }
    }


    /* in case the usernameOrEmail config don't work use this impl v
    @Transactional
    public UserDetails loadUserByUsername(String username, String email) throws UsernameNotFoundException {
        Optional<AdminEntity> userOptional = adminRepository.findUserByUsername(username);
        if(userOptional.isEmpty()){
            throw new UsernameNotFoundException("La credencial proporcionada no existe en el sistema");
        }
        userOptional = adminRepository.findUserByEmail(email);
        if(userOptional.isEmpty()){
            throw new UsernameNotFoundException("La credencial proporcionada no existe en el sistema");
        }
        UserDetails user = User.withUsername(userOptional.get().getUsername())
                .password(userOptional.get().getPwd())
                .authorities("USER").build();
        return user;
    }
     */
}

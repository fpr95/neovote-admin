package com.digiteo.neovoteIV.security.core.userdetail;

import com.digiteo.neovoteIV.model.jpa.data.UserEntity;
import com.digiteo.neovoteIV.model.jpa.repository.UserRepository;
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

    UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    //@Transactional
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        Optional<UserEntity> userOptional = userRepository.findUserByUsernameOrEmail(usernameOrEmail);
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


    //in case the usernameOrEmail config don't work use this impl v
    //@Transactional
    //public UserDetails loadUserByUsername(String username, String email) throws UsernameNotFoundException {
    //    Optional<UserEntity> userOptional = userRepository.findUserByUsername(username);
    //    if(userOptional.isEmpty()){
    //        throw new UsernameNotFoundException("La credencial proporcionada no existe en el sistema");
    //    }
    //    userOptional = userRepository.findUserByEmail(email);
    //    if(userOptional.isEmpty()){
    //        throw new UsernameNotFoundException("La credencial proporcionada no existe en el sistema");
    //    }
    //    UserDetails user = User.withUsername(userOptional.get().getUsername())
    //            .password(userOptional.get().getPwd())
    //            .authorities("USER").build();
    //    return user;
    //}
}

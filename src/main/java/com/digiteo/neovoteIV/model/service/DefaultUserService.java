package com.digiteo.neovoteIV.model.service;

import com.digiteo.neovoteIV.model.jpa.data.UserEntity;
import com.digiteo.neovoteIV.model.jpa.repository.UserRepository;
import com.digiteo.neovoteIV.system.exception.EmailAlreadyExistException;
import com.digiteo.neovoteIV.system.exception.UsernameAlreadyExistException;
import com.digiteo.neovoteIV.web.data.model.UserData;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service("userService")
@AllArgsConstructor // check if this autowire the fields, if not,
// try 'onConstructor' attribute, if still not work,
// create custom constructor with @Autowired annotation
public class DefaultUserService implements UserService {

    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

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

    private void encodePwd(UserData source, UserEntity target) {
        target.setPwd(passwordEncoder.encode(source.getPwd()));
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

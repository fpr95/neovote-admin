package com.digiteo.neovoteIV.model.service.impl;

import com.digiteo.neovoteIV.model.jpa.data.Election;
import com.digiteo.neovoteIV.model.jpa.data.ElectionRoll;
import com.digiteo.neovoteIV.model.jpa.data.VoterEntity;
import com.digiteo.neovoteIV.model.jpa.repository.ElectionRollRepository;
import com.digiteo.neovoteIV.model.jpa.repository.VoterRepository;
import com.digiteo.neovoteIV.model.service.AdminService;
import com.digiteo.neovoteIV.model.service.ElectionRollService;
import com.digiteo.neovoteIV.model.service.VoterService;
import com.digiteo.neovoteIV.system.exception.EmailAlreadyExistException;
import com.digiteo.neovoteIV.system.exception.UsernameAlreadyExistException;
import com.digiteo.neovoteIV.web.data.model.RollRegistrationData;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DefaultElectionRollService implements ElectionRollService {

    private VoterService voterService;
    private VoterRepository voterRepository;
    private ElectionRollRepository electionRollRepository;
    private AdminService adminService;

    @Override
    public boolean checkIfUserIsRegistered(String username, String email) throws EmailAlreadyExistException, UsernameAlreadyExistException {
        if(email != null){
            try{
                voterService.checkIfVoterExist(username, email);
            } catch(UsernameAlreadyExistException ex){
                return true;
            } catch (EmailAlreadyExistException ex){
                return true;
            }
        }
        return false;
    }

    //this impl doesn't need to handle exceptions
    @Override
    public boolean checkIfUserIsRegisteredPlus(String username){
        boolean isRegistered = false;
        if(username != null){
            if(voterRepository.findVoterByUsernameOrEmail(username).isPresent()){
                isRegistered = true;
            }
        }
        return isRegistered;
    }

    @Override
    public boolean addToRoll(RollRegistrationData rrd, Election election, Principal principal) {
        if(adminService.authenticateAdminPassword(principal, rrd.getPwd())){
            String[] roll = rrd.getRoll();
            for(String s:roll){
                // checking for double values !!!!
                ElectionRoll er = new ElectionRoll(s, election);
                if(voterRepository.findVoterByUsernameOrEmail(s).isPresent()){
                    er.setRegistered(true);
                    er.setEmail(voterRepository.findVoterByUsernameOrEmail(s).get().getEmail());
                } else {
                    er.setRegistered(false);
                }
                electionRollRepository.save(er);
                election.getRoll().add(er);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void deleteUserInRoll(String username){
        Optional<ElectionRoll> ero = electionRollRepository.findRollUsername(username);
        if(ero.isPresent()){
            ElectionRoll er = ero.get();
            Election e = er.getTargetElection();
            e.getRoll().remove(er);
            electionRollRepository.delete(er);
        }
    }
}

package com.digiteo.neovoteIV.web.controller;

import com.digiteo.neovoteIV.model.jpa.data.Election;
import com.digiteo.neovoteIV.model.jpa.data.ElectionRoll;
import com.digiteo.neovoteIV.model.service.ElectionRollService;
import com.digiteo.neovoteIV.model.service.ElectionService;
import com.digiteo.neovoteIV.model.service.VoterService;
import com.digiteo.neovoteIV.system.exception.EmailAlreadyExistException;
import com.digiteo.neovoteIV.system.exception.UsernameAlreadyExistException;
import com.digiteo.neovoteIV.web.data.model.ElectionListData;
import com.digiteo.neovoteIV.web.data.model.ElectionRollData;
import com.digiteo.neovoteIV.web.data.model.RollRegistrationData;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequestMapping("/auth")
@Controller
@AllArgsConstructor
public class RollsController {

    private ElectionService electionService;
    private ElectionRollService electionRollService;

    @GetMapping("/rolls")
    public String rollsView(final Model model){
        List<ElectionListData> eld = electionService.getElectionsList();
        for(ElectionListData e:eld){
            Election election = electionService.getElectionByTitle(e.getTitle());
            e.setRoll(election.getRoll().size());
        }
        model.addAttribute("electionsList", eld);
        return "auth/rolls";
    }

    @GetMapping("/election-roll")
    public String electionRollView(@RequestParam(value = "uid", required = false) String title,
                                   @RequestParam(value = "errorInPassword", required = false) boolean flag,
                                   //@RequestParam(value = "eliminatedUsername", required = false) String eliminatedUsername,
                                   final Model model) throws EmailAlreadyExistException, UsernameAlreadyExistException {
        Election e = electionService.getElectionByTitle(title);
        Collection<ElectionRoll> electionRoll = e.getRoll();
        Set<ElectionRollData> rollSet = new HashSet<>();
        for(ElectionRoll er:electionRoll){
            ElectionRollData erd = new ElectionRollData();
            erd.setVoterUsername(er.getVoterUsername());
            erd.setEmail(er.getEmail());
            if(!er.isRegistered()){
                if(electionRollService.checkIfUserIsRegistered(erd.getVoterUsername(), erd.getEmail())){
                    erd.setRegistered(true);
                } else {
                    erd.setRegistered(false);
                }
            } else {
                erd.setRegistered(er.isRegistered());
            }
            erd.setCreatedAt(er.getCreationTimestamp());
            rollSet.add(erd);
        }
        //delete this block if don't work the redirect of the flag and the eliminatedUsername
        //if(eliminatedUsername != null){
        //    model.addAttribute("eliminatedUsername", eliminatedUsername);
        //}

        //model.addAttribute("eliminatedUserFlag", flag);
        //delete this block if don't work the redirect of the flag and the eliminatedUsername

        model.addAttribute("errorInPassword", flag);
        model.addAttribute("rollData", new RollRegistrationData());
        model.addAttribute("electionTitle", e.getTitle());
        model.addAttribute("rollSet", rollSet);

        return "auth/election-roll";
    }

    @PostMapping("/election-roll")
    public String addVoter2Roll(final @Valid @ModelAttribute("proposalData")RollRegistrationData rrd,
                                final BindingResult bindingResult,
                                @RequestParam(value = "et", required = false) String electionTitle,
                                Principal principal,
                                final Model model,
                                final RedirectAttributes redirectAttr) throws EmailAlreadyExistException, UsernameAlreadyExistException {
        Election e = electionService.getElectionByTitle(electionTitle);
        if(bindingResult.hasErrors()){
            Collection<ElectionRoll> electionRoll = e.getRoll();
            Set<ElectionRollData> rollSet = new HashSet<>();
            for(ElectionRoll er:electionRoll){
                ElectionRollData erd = new ElectionRollData();
                erd.setVoterUsername(er.getVoterUsername());
                erd.setEmail(er.getEmail());
                if(!er.isRegistered()){
                    if(electionRollService.checkIfUserIsRegistered(erd.getVoterUsername(), erd.getEmail())){
                        erd.setRegistered(true);
                    } else {
                        erd.setRegistered(false);
                    }
                } else {
                    erd.setRegistered(er.isRegistered());
                }
                erd.setCreatedAt(er.getCreationTimestamp());
                rollSet.add(erd);
            }

            model.addAttribute("rollRegistrationValidationErrors", true);
            model.addAttribute("electionTitle", electionTitle);
            model.addAttribute("rollData", rrd);
            //find another way to do all this if there's an error!
            return "auth/election-roll";
        }
        boolean flag = electionRollService.addToRoll(rrd, e, principal);
        if (!flag) {
            redirectAttr.addAttribute("errorInPassword", true);
        }
        redirectAttr.addAttribute("uid", electionTitle);
        return "redirect:/auth/election-roll";
    }

    @RequestMapping(value = "/election-roll", method = RequestMethod.DELETE, params = {"et", "uname"})
    public String deleteUserFromRoll(@RequestParam(value = "et", required = false) String electionTitle,
                                     @RequestParam(value = "uname", required = false) String username,
                                     Model model,
                                     RedirectAttributes redirectAttr) throws EmailAlreadyExistException, UsernameAlreadyExistException {
        electionRollService.deleteUserInRoll(username);
        boolean flag = true;

        Election e = electionService.getElectionByTitle(electionTitle);
        Collection<ElectionRoll> electionRoll = e.getRoll();
        Set<ElectionRollData> rollSet = new HashSet<>();
        for(ElectionRoll er:electionRoll){
            ElectionRollData erd = new ElectionRollData();
            erd.setVoterUsername(er.getVoterUsername());
            erd.setEmail(er.getEmail());
            if(!er.isRegistered()){
                if(electionRollService.checkIfUserIsRegistered(erd.getVoterUsername(), erd.getEmail())){
                    erd.setRegistered(true);
                } else {
                    erd.setRegistered(false);
                }
            } else {
                erd.setRegistered(er.isRegistered());
            }
            erd.setCreatedAt(er.getCreationTimestamp());
            rollSet.add(erd);
        }

        model.addAttribute("eliminatedUserFlag", flag);
        model.addAttribute("eliminatedUsername", username);
        model.addAttribute("rollData", new RollRegistrationData());
        model.addAttribute("electionTitle", electionTitle);
        model.addAttribute("rollSet", rollSet);

        return "auth/election-roll";
    }

    //redirectAttr.addAttribute("eliminatedUserFlag", flag);
    //redirectAttr.addAttribute("eliminatedUsername", username);
    //redirectAttr.addAttribute("uid", electionTitle);
    //redirectAttr.addAttribute(model); // idk if it's possible to redirect the model so test this
    //redirectAttr.addFlashAttribute(model);
}

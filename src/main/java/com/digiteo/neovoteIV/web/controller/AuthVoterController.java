package com.digiteo.neovoteIV.web.controller;

import com.digiteo.neovoteIV.model.jpa.data.VoterEntity;
import com.digiteo.neovoteIV.model.service.FileService;
import com.digiteo.neovoteIV.model.service.VoterService;
import com.digiteo.neovoteIV.web.data.model.VoterData;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RequestMapping("/auth-voter")
@Controller
@AllArgsConstructor
public class AuthVoterController {

    private FileService fileService;
    private VoterService voterService;

    @RequestMapping("/welcome-voter")
    public String loggedInVoter(Principal principal, final Model model){
        VoterEntity v = voterService.getVoterByUsernameOrEmail(principal.getName());
        VoterData data = new VoterData();
        data.setProfileImagePath(v.getProfileImagePath());

        model.addAttribute("voterData", data);
        return "auth-voter/welcome-voter";
    }

    @GetMapping("/faq-voter")
    public String faqVoter(Principal principal, final Model model){
        VoterEntity v = voterService.getVoterByUsernameOrEmail(principal.getName());
        VoterData data = new VoterData();
        data.setProfileImagePath(v.getProfileImagePath());

        model.addAttribute("voterData", data);
        return "auth-voter/faq-voter";
    }

    @GetMapping("/user-profile")
    public String profileVoter(final Model model, Principal principal){
        VoterEntity v = voterService.getVoterByUsernameOrEmail(principal.getName());
        VoterData data = new VoterData();
        data.setFirstName(v.getFirstName());
        data.setLastName(v.getLastName());
        data.setUsername(v.getUsername());
        if(v.getProfileImagePath() != null){
            data.setProfileImagePath(v.getProfileImagePath());
        } else {
            data.setProfileImagePath(null);
        }
        data.setEmail(v.getEmail());
        if(!v.getGender().isEmpty()){
            data.setGender(v.getGender());
        } else {
            data.setGender("NO ESPECIFICADO");
        }

        model.addAttribute("voterData", data);
        return "auth-voter/user-profile";
    }

    @PostMapping("/user-profile")
    public String addVoterImage(@RequestParam("file") MultipartFile file, Principal principal) throws IOException, InterruptedException {
        fileService.uploadFile(file, null, null, principal.getName(), null);
        Thread.sleep(2000);
        return "redirect:/auth-voter/user-profile";
    }

    @DeleteMapping("/delete-profile")
    public String deleteVoterProfile(Principal principal){
        voterService.deleteVoter(principal.getName());
        return "redirect:/login";
    }
}

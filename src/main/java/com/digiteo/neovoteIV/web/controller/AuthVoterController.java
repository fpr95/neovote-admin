package com.digiteo.neovoteIV.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/auth-voter")
@Controller
public class AuthVoterController {

    @RequestMapping("/welcome-voter")
    public String loggedInVoter(){
        return "auth-voter/welcome-voter";
    }
}

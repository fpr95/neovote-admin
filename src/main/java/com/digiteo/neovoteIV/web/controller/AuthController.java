package com.digiteo.neovoteIV.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/account")
@Controller
public class AuthController {

    @GetMapping("/homeuser")
    public String loggedInUser(){
        return "account/homeuser";
    }
}

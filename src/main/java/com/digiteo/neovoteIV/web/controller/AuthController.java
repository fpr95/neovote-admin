package com.digiteo.neovoteIV.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/auth")
@Controller
public class AuthController {

    // this endpoint redirects to the default-success-logged-in ADMIN view, VOTERS must have another one.
    @GetMapping("/welcome-admin")
    public String loggedInUser(){
        return "auth/welcome-admin";
    }

    @GetMapping("/calendar-admin")
    public String calendarView() { return "auth/calendar-admin"; }
}

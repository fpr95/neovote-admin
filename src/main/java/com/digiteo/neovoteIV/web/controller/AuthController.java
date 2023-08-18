package com.digiteo.neovoteIV.web.controller;

import com.digiteo.neovoteIV.model.jpa.data.AdminEntity;
import com.digiteo.neovoteIV.model.jpa.data.VoterEntity;
import com.digiteo.neovoteIV.model.service.AdminService;
import com.digiteo.neovoteIV.model.service.FileService;
import com.digiteo.neovoteIV.web.data.model.AdminData;
import com.digiteo.neovoteIV.web.data.model.VoterData;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RequestMapping("/auth")
@Controller
@AllArgsConstructor
public class AuthController {

    private AdminService adminService;
    private FileService fileService;

    @GetMapping("/welcome-admin")
    public String loggedInUser(Principal principal, final Model model){
        AdminEntity ae = adminService.getUserByUsernameOrEmail(principal.getName());
        AdminData data = new AdminData();
        data.setProfileImagePath(ae.getProfileImagePath());

        model.addAttribute("adminData", data);
        return "auth/welcome-admin";
    }

    @GetMapping("/calendar-admin")
    public String calendarView(Principal principal, final Model model) {
        AdminEntity ae = adminService.getUserByUsernameOrEmail(principal.getName());
        AdminData data = new AdminData();
        data.setProfileImagePath(ae.getProfileImagePath());

        model.addAttribute("adminData", data);
        return "auth/calendar-admin";
    }

    @GetMapping("/faq-admin")
    public String faqView(Principal principal, final Model model) {
        AdminEntity ae = adminService.getUserByUsernameOrEmail(principal.getName());
        AdminData data = new AdminData();
        data.setProfileImagePath(ae.getProfileImagePath());

        model.addAttribute("adminData", data);
        return "auth/faq-admin";
    }

    @GetMapping("/admin-profile")
    public String profileAdmin(final Model model, Principal principal){
        AdminEntity a = adminService.getUserByUsernameOrEmail(principal.getName());
        AdminData data = new AdminData();
        data.setFirstName(a.getFirstName());
        data.setLastName(a.getLastName());
        data.setUsername(a.getUsername());
        if(a.getProfileImagePath() != null){
            data.setProfileImagePath(a.getProfileImagePath());
        } else {
            data.setProfileImagePath(null);
        }
        data.setEmail(a.getEmail());
        if(!a.getGender().isEmpty()){
            data.setGender(a.getGender());
        } else {
            data.setGender("NO ESPECIFICADO");
        }

        model.addAttribute("adminData", data);
        return "auth/admin-profile";
    }

    @PostMapping("/admin-profile")
    public String addAdminImage(@RequestParam("file") MultipartFile file, Principal principal) throws IOException, InterruptedException {
        fileService.uploadFile(file, null, null, null, principal.getName());
        Thread.sleep(2000);
        return "redirect:/auth/admin-profile";
    }

    @DeleteMapping("/delete-profile")
    public String deleteAdminProfile(Principal principal) throws Exception {
        adminService.deleteAdminProfile(principal.getName());
        return "redirect:/login-admin";
    }
}

package com.digiteo.neovoteIV.web;

import com.digiteo.neovoteIV.model.service.AdminService;
import com.digiteo.neovoteIV.system.exception.EmailAlreadyExistException;
import com.digiteo.neovoteIV.system.exception.InvalidTokenException;
import com.digiteo.neovoteIV.system.exception.UsernameAlreadyExistException;
import com.digiteo.neovoteIV.web.data.model.ResetPasswordData;
import com.digiteo.neovoteIV.web.data.model.AdminData;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;

@RequestMapping("/")
@Controller
//@Validated -> this annotation was the cause of the field(s) constraint-errors not showing up in the view!
@AllArgsConstructor
public class HomeController {

    private AdminService adminService;
    private MessageSource messageSource;

    @GetMapping("/login")
    public String login(
            @RequestParam(value = "tokenError", required = false) String tokenError,
            @RequestParam(value = "verifiedAccountMsg", required = false) String verifiedAccountMsg,
            @RequestParam(value = "resetPasswordMsg", required = false) String resetPasswordMsg,
            @RequestParam(value = "unknownId", required = false) String unknownId,
            final Model model){
        if(tokenError != null && !tokenError.isEmpty()){
            model.addAttribute("tokenError", tokenError);
        }
        if(verifiedAccountMsg != null && !verifiedAccountMsg.isEmpty()){
            model.addAttribute("verifiedAccountMsg", verifiedAccountMsg);
        }
        if(resetPasswordMsg != null && !resetPasswordMsg.isEmpty()){
            model.addAttribute("resetPasswordMsg", resetPasswordMsg);
        }
        if(unknownId != null && !unknownId.isEmpty()){
            model.addAttribute("unknownId", unknownId);
        }
        model.addAttribute("forgotPassword", new ResetPasswordData());
        return "/login";
    }

    @GetMapping("/register")
    public String register(@RequestParam(value = "registrationMsg", required = false) String verificationMsg, final Model model){
        if(verificationMsg != null && !verificationMsg.isEmpty()){
            model.addAttribute("registrationMsg",
                    messageSource.getMessage("user.registration.verification.email.msg", null, LocaleContextHolder.getLocale()));
        }
        model.addAttribute("userData", new AdminData());
        return "/register";
    }

    @GetMapping("/faq-public")
    public String publicFaq(){ return "/faq-public"; }

    @PostMapping("/register")
    public String userRegistration(final @Valid AdminData userData, final BindingResult bindingResult,final RedirectAttributes redirectAttr, final Model model) {
        if(bindingResult.hasErrors()){
            model.addAttribute("registrationForm", userData);
            return "/register"; // find a way to return a field-error or registration form error or something like that to make thymeleaf show a message
        }
        try{
            adminService.register(userData);
        }catch (UsernameAlreadyExistException e){
            bindingResult.rejectValue(
                    "username",
                    "userData.username",
                    "Ya existe un usuario registrado con ese nombre de usuario");
            model.addAttribute("registrationForm", userData);
            return "/register";
        }catch (EmailAlreadyExistException e){
            bindingResult.rejectValue(
                    "email",
                    "userData.email",
                    "Ya existe un usuario registrado con ese correo");
            model.addAttribute("registrationForm", userData);
            return "/register";
        }
        redirectAttr.addFlashAttribute("registrationMsg",
                messageSource.getMessage("user.registration.verification.email.msg",null, LocaleContextHolder.getLocale()));
        return "redirect:/register";
    }

    @GetMapping("emails/verify")
    public String verifyUser(@RequestParam(required = false) String token, final Model model, RedirectAttributes redirectAttr){
        if(StringUtils.isEmpty(token)){
            redirectAttr.addFlashAttribute("tokenError",
                    messageSource.getMessage("user.registration.verification.email.noToken", null, LocaleContextHolder.getLocale()));
            return "redirect:/login";
        }
        try {
            adminService.verifyUser(token);
        } catch(InvalidTokenException ex) {
            redirectAttr.addFlashAttribute("tokenError",
                    messageSource.getMessage("user.registration.verification.email.invalidToken", null, LocaleContextHolder.getLocale()));
            return "redirect:/login";
        }
        redirectAttr.addFlashAttribute("verifiedAccountMsg",
                messageSource.getMessage("user.registration.verification.email.verifiedAccount", null, LocaleContextHolder.getLocale()));
        return "redirect:/login";
    }

    /*
    @PostMapping("/register")
    public String userRegistrationPlus(final @Valid AdminData userData, final BindingResult bindingResult, final Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("registrationForm", userData);
            return "/register";
        }
        try {
            adminService.registerPlus(userData);
        } catch (UsernameAlreadyExistException e) {
            // check if the errorCode "userData.username" works for existent email validation : DON'T WORK
            bindingResult.rejectValue(null, "userData.username", "Ya existe un usuario registrado con esas credenciales");
            model.addAttribute("registrationForm", userData);
            return "/register";
        }
        return "redirect:/login?success";
    }
     */
}

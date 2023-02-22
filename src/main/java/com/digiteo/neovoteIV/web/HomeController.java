package com.digiteo.neovoteIV.web;

import com.digiteo.neovoteIV.model.service.UserService;
import com.digiteo.neovoteIV.system.exception.EmailAlreadyExistException;
import com.digiteo.neovoteIV.system.exception.UsernameAlreadyExistException;
import com.digiteo.neovoteIV.web.data.user.UserData;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/")
@Controller
//@Validated -> this annotation was the cause of the field(s) constraint-errors not showing up in the view!!!!
@AllArgsConstructor // check if this autowire the fields, if not,
// try 'onConstructor' attribute, if still not work,
// create custom constructor with @Autowired annotation
public class HomeController {

    private UserService userService;

    @GetMapping("/home")
    public String register(final Model model){
        model.addAttribute("userData", new UserData());
        return "/home";
    }

    @PostMapping("/home")
    public String userRegistration(final @Valid UserData userData, final BindingResult bindingResult, final Model model) {
        if(bindingResult.hasErrors()){
            model.addAttribute("registrationForm", userData);
            return "/home"; // find a way to return a field-error or registration form error or something like that to make thymeleaf show a message
        }
        try{
            userService.register(userData);
        }catch (UsernameAlreadyExistException e){
            bindingResult.rejectValue("username", "userData.username", "Ya existe un usuario registrado con ese nombre de usuario");
            model.addAttribute("registrationForm", userData);
            return "/home";
        }catch (EmailAlreadyExistException e){
            bindingResult.rejectValue("email", "userData.email", "Ya existe un usuario registrado con ese correo");
            model.addAttribute("registrationForm", userData);
            return "/home";
        }
        return "redirect:/home?success"; /* REDIRECT + check if this goes in the AppConstants*/
    }

    public String userRegistrationPlus(final @Valid UserData userData, final BindingResult bindingResult, final Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("registrationForm", userData);
            return "/home"; // find a way to return a field-error or registration form error or something like that to make thymeleaf show an alert message
        }
        try {
            userService.registerPlus(userData);
        } catch (UsernameAlreadyExistException e) {
            // check if the errorCode "userData.username" works for existent email validation
            bindingResult.rejectValue(null, "userData.username", "Ya existe un usuario registrado con esas credenciales");
            model.addAttribute("registrationForm", userData);
            return "/home";
        }
        return "redirect:/home?success";
    }
}

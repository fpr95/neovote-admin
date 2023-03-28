package com.digiteo.neovoteIV.web.controller;

import com.digiteo.neovoteIV.model.service.UserAccountService;
import com.digiteo.neovoteIV.system.exception.InvalidTokenException;
import com.digiteo.neovoteIV.system.exception.UnknownIdentifierException;
import com.digiteo.neovoteIV.web.data.model.ResetPasswordData;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;
import java.util.Objects;

@RequestMapping("/pwd")
@Controller
public class ResetPasswordController {

    private final String LOGIN_REDIRECT = "redirect:/login";
    private final String MSG1 = "resetPasswordMsg";
    private final String MSG2 = "tokenError";
    private final String MSG3 = "passwordUpdateMsg";
    private MessageSource messageSource;
    private UserAccountService userAccountService;

    @Autowired
    public ResetPasswordController(MessageSource messageSource,
                                   UserAccountService userAccountService) {
        this.messageSource = messageSource;
        this.userAccountService = userAccountService;
    }

    @PostMapping("/request")
    public String resetPassword(final ResetPasswordData data, RedirectAttributes redirectAttr){
        try {
            userAccountService.forgottenPassword(data.getEmail());
        } catch (UnknownIdentifierException ex) {
        //} catch (UnknownIdentifierException | EntityNotFoundException ex) {
            ex.printStackTrace();  //redirect flash attribute "unknownId" and log the error in login view
        } catch (EntityNotFoundException ex) {
            ex.printStackTrace();
            redirectAttr.addFlashAttribute("unknownId",
                    messageSource.getMessage("user.forgotpwd.unknownid.msg", null, LocaleContextHolder.getLocale()));
            return LOGIN_REDIRECT;
        }
        redirectAttr.addFlashAttribute(MSG1,
                messageSource.getMessage("user.forgotpwd.msg", null, LocaleContextHolder.getLocale()));
        return LOGIN_REDIRECT;
    }

    @GetMapping("/change")
    public String changePassword(@RequestParam(required = false) String token,@RequestParam(value = "invalidPassword", required = false) String invalidPassword, final RedirectAttributes redirectAttr, final Model model){

        /* CHANGE ALL THIS FOR A try-catch WITH DefaultUserAccountService#verifyResetPasswordToken METHOD
        if(token.isEmpty()){
            redirectAttr.addFlashAttribute(MSG2,
                    messageSource.getMessage("user.registration.verification.email.noToken", null, LocaleContextHolder.getLocale()));
            return LOGIN_REDIRECT;
        }
        if(Objects.isNull(token)){
            redirectAttr.addFlashAttribute(MSG2,
                    messageSource.getMessage("user.registration.verification.email.invalidToken", null, LocaleContextHolder.getLocale()));
            return LOGIN_REDIRECT;
        }
        */
        try {
            userAccountService.verifyResetPasswordToken(token);
        } catch (InvalidTokenException ex) {
            redirectAttr.addFlashAttribute(MSG2,
                    messageSource.getMessage("user.registration.verification.email.invalidToken", null, LocaleContextHolder.getLocale()));
            return LOGIN_REDIRECT;
        }

        if(invalidPassword != null && !invalidPassword.isEmpty()){
            model.addAttribute("invalidPassword", invalidPassword);
        }

        ResetPasswordData data = new ResetPasswordData();
        data.setToken(token);
        setResetPasswordFrom(data, model);
        return "/account/change-password";
    }

    @PostMapping(value = "/change")
    public String changePassword(final @Valid ResetPasswordData data, final BindingResult bindingResult, final Model model) {
        if(bindingResult.hasErrors()){
            bindingResult.rejectValue(
                    "password",
                    "forgotPassword.password",
                    "{user.registration.validation.pwd}");
            model.addAttribute("forgotPassword", data);
            model.addAttribute("invalidPassword",
                    messageSource.getMessage("user.registration.validation.pwd", null, LocaleContextHolder.getLocale()));
            return "/account/change-password"; // find a way to return a field-error or registration form error or something like that to make thymeleaf show a message
        }
        try {
            userAccountService.updatePassword(data.getPassword(), data.getToken());
        } catch (InvalidTokenException | UnknownIdentifierException ex) {
            model.addAttribute(MSG2,
                    messageSource.getMessage("user.registration.verification.email.invalidToken", null, LocaleContextHolder.getLocale()));
            setResetPasswordFrom(new ResetPasswordData(), model);
            return "/account/change-password";
        }
        model.addAttribute(MSG3,
                messageSource.getMessage("user.password.updated.msg", null, LocaleContextHolder.getLocale()));
        setResetPasswordFrom(new ResetPasswordData(), model);
        return "account/change-password";
    }

    private void setResetPasswordFrom(ResetPasswordData data, final Model model){
        model.addAttribute("forgotPassword", data);
    }
}

package com.digiteo.neovoteIV.email.context;

import com.digiteo.neovoteIV.model.jpa.data.UserEntity;
import org.springframework.web.util.UriComponentsBuilder;

public class AccountVerificationEmailContext extends AbstractEmailContext {

    private String token;

    @Override
    public <T> void init(T context){
        //change name of the obj to voter once implemented the voters layer
        UserEntity user = (UserEntity) context; //here goes the voter/admin information
        put("firstName", user.getFirstName());
        setTemplateLocation("/emails/email-verification");
        setSubject("Verifique su cuenta para continuar");
        setFrom("neovote"); // this email account should be placed in the application.yml and should be like 'no-reply@neovote.cl'
        setTo(user.getEmail());
    }

    public void setToken(String token) {
        this.token = token;
        put("token", token);
    }

    public void buildVerificationUrl(final String baseUrl, final String token) {
        final String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/emails/verify").queryParam("token", token).toUriString();
        put("verificationURL", url);
    }
}

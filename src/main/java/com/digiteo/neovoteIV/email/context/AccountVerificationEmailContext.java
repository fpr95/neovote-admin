package com.digiteo.neovoteIV.email.context;

import com.digiteo.neovoteIV.model.jpa.data.AdminEntity;
import com.digiteo.neovoteIV.model.jpa.data.VoterEntity;
import org.springframework.web.util.UriComponentsBuilder;

public class AccountVerificationEmailContext extends AbstractEmailContext {

    private String token;

    public void setToken(String token) {
        this.token = token;
        put("token", token);
    }

    @Override
    public <T> void init(T context){
        AdminEntity user = (AdminEntity) context; //admin information
        put("firstName", user.getFirstName());
        setTemplateLocation("/emails/email-verification");
        setSubject("Verifique su cuenta para continuar");
        setFrom("neovote"); // this could be placed in the application.yml and should be like 'no-reply@neovote.cl'
        setTo(user.getEmail());
    }

    @Override
    public <T> void initVoter(T context){
        VoterEntity voter = (VoterEntity) context; //voter info
        put("firstName", voter.getFirstName());
        setTemplateLocation("/emails/email-verification");
        setSubject("Verifique su cuenta para continuar");
        setFrom("neovote");
        setTo(voter.getEmail());
    }

    public void buildVerificationUrl(final String baseUrl, final String token) {
        final String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/emails/verify").queryParam("token", token).toUriString();
        put("verificationURL", url);
    }
}

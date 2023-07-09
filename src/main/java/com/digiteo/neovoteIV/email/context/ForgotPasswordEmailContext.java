package com.digiteo.neovoteIV.email.context;

import com.digiteo.neovoteIV.model.jpa.data.AdminEntity;
import com.digiteo.neovoteIV.model.jpa.data.VoterEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.util.UriComponentsBuilder;

@NoArgsConstructor
@Getter
@Setter
public class ForgotPasswordEmailContext extends AbstractEmailContext {

    private String token;

    public void setToken(String token) {
        this.token = token;
        put("token", token);
    }

    @Override
    public <T> void init(T context){
        //change name of the obj to voter once implemented the voters layer
        AdminEntity user = (AdminEntity)context; //here goes the voter/admin information
        put("firstName", user.getFirstName());
        setTemplateLocation("/emails/email-forgot-password");
        setSubject("Reestablecer contraseña");
        setFrom("lautaro.pipe@gmail.com"); // this email account should be placed in the application.yml and should be like 'no-responder@neovote.cl'
        setTo(user.getEmail());
    }

    @Override
    public <T> void initVoter(T context) {
        //change name of the obj to voter once implemented the voters layer
        VoterEntity voter = (VoterEntity) context; //here goes the voter/admin information
        put("firstName", voter.getFirstName());
        setTemplateLocation("/emails/email-forgot-password");
        setSubject("Reestablecer contraseña");
        setFrom("neovote"); // this email account should be placed in the application.yml and should be like 'no-reply@neovote.cl'
        setTo(voter.getEmail());
    }

    public void buildVerificationUrl(String baseUrl, String token){
        final String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/pwd/change").queryParam("token", token).toUriString();
        put("resetPasswordURL", url);
    }
}

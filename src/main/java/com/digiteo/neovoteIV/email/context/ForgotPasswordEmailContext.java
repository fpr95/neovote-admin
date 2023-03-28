package com.digiteo.neovoteIV.email.context;

import com.digiteo.neovoteIV.model.jpa.data.UserEntity;
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
        UserEntity user = (UserEntity) context; //here goes the voter/admin information
        put("firstName", user.getFirstName());
        setTemplateLocation("/emails/email-forgot-password");
        setSubject("Reestablecer contraseña");
        setFrom("neovote"); // this email account should be placed in the application.yml and should be like 'no-reply@neovote.cl'
        setTo(user.getEmail());
    }

    public void buildVerificationUrl(String baseUrl, String token){
        final String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/pwd/change").queryParam("token", token).toUriString();
        put("resetPasswordURL", url);
    }
}

package com.digiteo.neovoteIV.web.data.model;

import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ResetPasswordData implements Serializable {

    @Pattern(
            regexp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z\\d-]+\\.)+[a-zA-Z]{2,6}$",
            message = "{user.registration.validation.email}"
    )
    private String email;
    private String token;
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&/-_+=*()])(?=\\S+$).{8,20}$",
            message = "{user.registration.validation.pwd}"
    )
    private String password;
    private String repeatPassword;
}

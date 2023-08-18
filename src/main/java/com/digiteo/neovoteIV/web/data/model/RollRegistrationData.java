package com.digiteo.neovoteIV.web.data.model;

import com.digiteo.neovoteIV.validation.UsernameMinLength;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RollRegistrationData {

    @UsernameMinLength(3)
    //@NotBlank(message = "${rollUser.registration.validation.notnull.username}")
    private String[] roll;

    @NotBlank(message = "${rollUser.registration.validation.pwd}")
    private String pwd;
}

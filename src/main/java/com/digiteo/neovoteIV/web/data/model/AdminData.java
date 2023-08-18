package com.digiteo.neovoteIV.web.data.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminData implements Serializable {

    //@NotBlank(message = "{user.registration.validation.firstName}")
    @Size(min = 2, message = "{user.registration.validation.firstName}")
    private String firstName;

    //@NotBlank(message = "{user.registration.validation.lastName}")
    @Size(min = 2, message = "{user.registration.validation.lastName}")
    private String lastName;

    //@NotBlank(message = "{user.registration.validation.username}")
    @Size(min = 3, message = "{user.registration.validation.username}")
    private String username;

    private String profileImagePath;

    //@NotBlank(message = "Ingrese su correo por favor")
    @Pattern(
            regexp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z\\d-]+\\.)+[a-zA-Z]{2,6}$",
            message = "{user.registration.validation.email}"
    )
    private String email;

    //@NotEmpty(message = "{user.registration.validation.pwd}")
    //@Size(min = 8, message = "La contraseña debe tener como mínimo 8 caracteres")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&/-_+=*()])(?=\\S+$).{8,20}$",
            message = "{user.registration.validation.pwd}"
    )
    private String pwd;

    private String gender;

}

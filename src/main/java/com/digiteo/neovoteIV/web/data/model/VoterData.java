package com.digiteo.neovoteIV.web.data.model;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoterData implements Serializable {

    @Size(min = 2, message = "{user.registration.validation.firstName}")//message = "Ingrese su nombre")
    private String firstName;

    @Size(min = 2, message = "{user.registration.validation.lastName}") // message = "Ingrese su apellido")
    private String lastName;

    @Size(min = 3, message = "{user.registration.validation.username}") //, message = "Ingrese un nombre de usuario de al menos 2 caracteres, por favor")
    private String username;

    private String profileImagePath;

    @Pattern(
            regexp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z\\d-]+\\.)+[a-zA-Z]{2,6}$",
            message = "{user.registration.validation.email}"
    )
    private String email;

    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&/-_+=*()])(?=\\S+$).{8,20}$",
            message = "{user.registration.validation.pwd}"
    )
    private String pwd;

    private String gender;
}

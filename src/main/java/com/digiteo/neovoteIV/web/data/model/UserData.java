package com.digiteo.neovoteIV.web.data.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
// @JsonInclude(JsonInclude.Include.NON_NULL)
public class UserData implements Serializable {

    //@NotBlank(message = "{user.registration.validation.firstName}")
    @Size(min = 2, message = "{user.registration.validation.firstName}")//message = "Ingrese su nombre")
    private String firstName;

    //@NotBlank(message = "{user.registration.validation.lastName}")
    @Size(min = 2, message = "{user.registration.validation.lastName}") // message = "Ingrese su apellido")
    private String lastName;

    //@NotBlank(message = "{user.registration.validation.username}")
    @Size(min = 3, message = "{user.registration.validation.username}") //, message = "Ingrese un nombre de usuario de al menos 2 caracteres, por favor")
    private String username;

    //@NotBlank(message = "Ingrese su correo por favor")
    //@Email(message = "{user.registration.validation.email}")
    @Pattern(
            regexp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z\\d-]+\\.)+[a-zA-Z]{2,6}$",
            message = "{user.registration.validation.email}"
            //message = "Ingrese un correo válido (@Pattern)"
    )
    private String email;

    //@NotEmpty(message = "{user.registration.validation.pwd}")
    //@Size(min = 8, message = "La contraseña debe tener como mínimo 8 caracteres")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&/-_+=*()])(?=\\S+$).{8,20}$",
            message = "{user.registration.validation.pwd}"
            //message = "La contraseña debe contener al menos una minúscula, una mayúscula, un número y un símbolo"
    )
    private String pwd;

    private String gender;

}

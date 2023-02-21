package com.digiteo.neovoteIV.web.data.user;

import com.fasterxml.jackson.annotation.JsonInclude;
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

    //@NotBlank(message = "{registration.validation.firstName}")
    @Size(min = 2, message = "{registration.validation.firstName}")//message = "Ingrese su nombre")
    private String firstName;

    //@NotBlank(message = "{registration.validation.lastName}")
    @Size(min = 2, message = "{registration.validation.lastName}") // message = "Ingrese su apellido")
    private String lastName;

    //@NotBlank(message = "{registration.validation.username}")
    @Size(min = 3, message = "{registration.validation.username}") //, message = "Ingrese un nombre de usuario de al menos 2 caracteres, por favor")
    private String username;

    //@NotBlank(message = "Ingrese su correo por favor")
    //@Email(message = "{registration.validation.email}")
    @Pattern(
            regexp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z\\d-]+\\.)+[a-zA-Z]{2,6}$",
            message = "{registration.validation.email}"
            //message = "Ingrese un correo válido (@Pattern)"
    )
    private String email;

    //@NotEmpty(message = "{registration.validation.pwd}")
    //@Size(min = 8, message = "La contraseña debe tener como mínimo 8 caracteres")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&/-_+=*()])(?=\\S+$).{8,20}$",
            message = "{registration.validation.pwd}"
            //message = "La contraseña debe contener al menos una minúscula, una mayúscula, un número y un símbolo"
    )
    private String pwd;

    private String gender;

}

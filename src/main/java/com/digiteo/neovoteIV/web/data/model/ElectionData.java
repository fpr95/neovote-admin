package com.digiteo.neovoteIV.web.data.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElectionData implements Serializable {

    @NotBlank(message = "{election.registration.validation.title}")
    @Size(min = 2, max = 70, message = "El Título debe tener de 2 a 70 caracteres")
    private String title;

    private String profileImagePath;

    @NotNull(message = "{election.registration.validation.topic}")
    //@Size(min = 2, message = "Debe contener al menos 2 caracteres.")
    private String topics;

    @NotBlank(message = "{election.registration.validation.briefDescription}")
    @Size(max = 100, message = "El campo 'Descripción Breve' puede tener como máximo 100 caracteres")
    private String briefDescription;

    @NotNull(message = "{election.registration.validation.initTimestamp}")
    @FutureOrPresent(message = "No se pudo establecer el inicio del proceso: La fecha/hora ya ha pasado.")
    @DateTimeFormat(pattern = "dd-MM-yyyy, HH:mm")
    private LocalDateTime initTimestamp;

    @NotNull(message = "{election.registration.validation.finishTimestamp}")
    @Future(message = "La fecha y/u hora estipulada no está bien ingresada, por favor revise este campo.")
    @DateTimeFormat(pattern = "dd-MM-yyyy, HH:mm")
    private LocalDateTime finishTimestamp;

    @Size(min = 10, max = 700, message = "{election.registration.validation.details}")
    private String details;

    private String electionStatus;
}

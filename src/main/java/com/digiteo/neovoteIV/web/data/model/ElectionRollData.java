package com.digiteo.neovoteIV.web.data.model;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElectionRollData {

    //@Size(min = 3, message = "{rollUser.registration.validation.length.username}")
    private String voterUsername;

    private String email;

    private boolean isRegistered;

    private LocalDateTime createdAt;

}

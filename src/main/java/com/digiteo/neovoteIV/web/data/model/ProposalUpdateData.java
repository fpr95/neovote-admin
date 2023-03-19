package com.digiteo.neovoteIV.web.data.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProposalUpdateData implements Serializable {

    @Size(max = 30, message = "{proposal.registration.validation.name}")
    private String name;

    //@Pattern(
    //        regexp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z\\d-]+\\.)+[a-zA-Z]{2,6}$",
    //        message = "{proposal.registration.validation.contactEmail}"
    //)
    @Email(
            regexp = "^(?:[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z\\d-]+\\.)+[a-zA-Z]{2,6})?$",
            message = "{proposal.registration.validation.contactEmail}"
    )
    private String contactEmail;

    @Size(max = 300, message = "{proposal.registration.validation.details}")
    private String details;

    private boolean visible;

}

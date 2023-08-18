package com.digiteo.neovoteIV.web.data.model;

import jakarta.validation.constraints.Email;
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

    //add validation constraints for each "(...)Id" field
    private String webId;

    private String facebookId;

    private String linkedinId;

    private String xId;

    private String instagramId;

    @Size(max = 700, message = "{proposal.registration.validation.details}")
    private String details;

    private boolean visible;

}

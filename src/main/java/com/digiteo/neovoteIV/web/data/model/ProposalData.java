package com.digiteo.neovoteIV.web.data.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProposalData implements Serializable {

    @NotBlank(message = "{proposal.registration.validation.name}")
    @Size(min = 2, message = "{proposal.registration.validation.name}")
    private String name;

    private String profileImagePath;

    @Pattern(
            regexp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z\\d-]+\\.)+[a-zA-Z]{2,6}$",
            message = "{proposal.registration.validation.contactEmail}"
    )
    private String contactEmail;

    //check how to add validation constraints for each "(...)Id" field
    private String webId;

    private String facebookId;

    private String linkedinId;

    private String xId;

    private String instagramId;

    @NotBlank(message = "{proposal.registration.validation.details}")
    @Size(max = 900, message = "{proposal.registration.validation.details}")
    private String details;

    private boolean visible;

    private LocalDateTime createdAt;
}

package com.digiteo.neovoteIV.web.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProposalResultData implements Serializable {

    private String name;
    private String email;
    private boolean isVisible;
    private int votes;
    private int percent;
    private LocalDateTime createdAt;

}

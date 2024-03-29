package com.digiteo.neovoteIV.web.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElectionListData implements Serializable {

    private String title;
    private String profileImagePath;
    private String[] topics;
    private String briefDescription;
    // try with 'ElectionStatus', if it doesn't work try convert it to String value
    private String status;
    private int roll;
    private String creatorUsername;
    private LocalDateTime initTimestamp;
    private LocalDateTime finishTimestamp;
}

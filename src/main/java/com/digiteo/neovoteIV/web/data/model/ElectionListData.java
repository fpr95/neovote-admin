package com.digiteo.neovoteIV.web.data.model;

import com.digiteo.neovoteIV.model.jpa.data.ElectionStatus;
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
    private String[] topics;
    private String briefDescription;
    // try with 'ElectionStatus', if it doesn't work try convert it to String value
    private String status;
    private String creatorUsername;
    private LocalDateTime initTimestamp;
    private LocalDateTime finishTimestamp;
}

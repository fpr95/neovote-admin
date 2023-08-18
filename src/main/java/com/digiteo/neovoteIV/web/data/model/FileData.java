package com.digiteo.neovoteIV.web.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileData {

    private String fileName;
    private String fileType;
    private String filePath;
}

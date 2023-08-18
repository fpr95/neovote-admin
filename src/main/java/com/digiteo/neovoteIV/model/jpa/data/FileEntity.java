package com.digiteo.neovoteIV.model.jpa.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "fileEntity")
@Table(name = "file_data")
public class FileEntity extends BaseEntity{

    private String fileName;
    private String fileType;
    private String filePath;
    private String adminUsername;
    private String voterUsername;
    private String electionTitle;
    private String proposalName;
}
package com.digiteo.neovoteIV.model.service;

import jakarta.annotation.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    void uploadFile(MultipartFile file,
                    @Nullable String proposalName,
                    @Nullable String electionTitle,
                    @Nullable String voterUsername,
                    @Nullable String adminUsername) throws IOException;
    byte[] downloadFile(String fileName) throws IOException;
}

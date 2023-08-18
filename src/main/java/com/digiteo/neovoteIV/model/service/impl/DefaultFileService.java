package com.digiteo.neovoteIV.model.service.impl;

import com.digiteo.neovoteIV.model.jpa.data.FileEntity;
import com.digiteo.neovoteIV.model.jpa.repository.FileRepository;
import com.digiteo.neovoteIV.model.service.*;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Array;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DefaultFileService implements FileService {

    private FileRepository fileRepository;
    private ProposalService proposalService;
    private AdminService adminService;
    private VoterService voterService;
    private ElectionService electionService;
    // set beginindex in 88, configure a new resource handler and modify AppSecurityConfig to allow access
    private final String FILE_PATH = System.getProperty("user.dir") + "\\src\\main\\uploads\\images\\";

    @Override // this method could be separated for each image to upload(i.e. proposal, voter, etc.)
    public void uploadFile(MultipartFile file,
                           @Nullable String proposalName,
                           @Nullable String electionTitle,
                           @Nullable String voterUsername,
                           @Nullable String adminUsername) throws IOException {
        String filePath = FILE_PATH + file.getOriginalFilename();
        file.transferTo(new File(filePath));
        FileEntity fe = FileEntity.builder()
                .fileName(file.getOriginalFilename())
                .fileType(file.getContentType())
                .filePath(filePath)
                .build();
        if(proposalName != null){
            fe.setProposalName(proposalName);
            proposalService.findProposal(proposalName).setProfileImagePath(filePath.substring(88).replace("\\", "/"));
        }
        if(electionTitle != null){
            fe.setElectionTitle(electionTitle);
            electionService.getElectionByTitle(electionTitle).setProfileImagePath(filePath.substring(88)); //beginindex was 98
        }
        if(voterUsername != null){
            fe.setVoterUsername(voterUsername);
            voterService.getVoterByUsernameOrEmail(voterUsername).setProfileImagePath(filePath.substring(88));
        }
        if(adminUsername != null){
            fe.setAdminUsername(adminUsername);
            adminService.getUserByUsernameOrEmail(adminUsername).setProfileImagePath(filePath.substring(88));
        }
        if(fe != null){
            fileRepository.save(fe);
        }
    }

    @Override
    public byte[] downloadFile(String fileName) throws IOException {
        Optional<FileEntity> fo = fileRepository.findByFileName(fileName);
        String filePath = fo.get().getFilePath();
        byte[] fileBytes = Files.readAllBytes(new File(filePath).toPath());
        return fileBytes;
    }
}

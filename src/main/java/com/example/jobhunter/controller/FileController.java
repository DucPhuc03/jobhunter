package com.example.jobhunter.controller;

import com.example.jobhunter.dto.response.ResUploadFileDTO;
import com.example.jobhunter.exception.StoreFileException;
import com.example.jobhunter.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class FileController {
    @Autowired
    FileService fileService;
    @Value("${hoidanit.upload-file.base-uri}")
    private String baseUri;
    @PostMapping("/files")
    public ResponseEntity<ResUploadFileDTO> uploadFile(@RequestParam("file")MultipartFile file, @RequestParam("folder") String folder)
            throws URISyntaxException, StoreFileException {
        if(file.isEmpty()){
            throw new StoreFileException("file khong ton tai");
        }
        String fileName=file.getOriginalFilename();
        List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "jpeg", "png", "doc", "docx");
        boolean isValidExtension = allowedExtensions.stream().anyMatch(item -> fileName.toLowerCase().endsWith(item));
        if(!isValidExtension){
            throw new StoreFileException("file khong dung dinh dang");
        }
        fileService.creareFolder(baseUri+folder);
        String uploadFileName= fileService.saveFolder(file,folder);
        ResUploadFileDTO res=new ResUploadFileDTO(uploadFileName, Instant.now());
        return ResponseEntity.status(HttpStatus.OK).body(res);

    }
}

package com.example.jobhunter.controller;

import com.example.jobhunter.dto.response.RestResponse;
import com.example.jobhunter.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class EmailController {
    @Autowired
    EmailService emailService;
    @GetMapping("/email")
    public ResponseEntity<RestResponse<String>> sendEmail() {
//        emailService.sendEmail();
//        emailService.sendEmailSync("nguyenpguc2003@gmail.com","test","hello",false,false);
        emailService.sendEmailFromTemplateSync("nguyenpguc2003@gmail.com","test","job");
        RestResponse<String> res = new RestResponse<String>();
        res.setData("hello");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}

package com.example.jobhunter.controller;

import com.example.jobhunter.dto.response.ResResumeCreateDTO;
import com.example.jobhunter.dto.response.ResResumeDTO;
import com.example.jobhunter.dto.response.ResUpdateResumeDTO;
import com.example.jobhunter.dto.response.ResultPaginationDTO;
import com.example.jobhunter.entity.Company;
import com.example.jobhunter.entity.Job;
import com.example.jobhunter.entity.Resume;
import com.example.jobhunter.entity.User;
import com.example.jobhunter.exception.IdInvalidException;
import com.example.jobhunter.service.ResumeService;
import com.example.jobhunter.service.UserService;
import com.example.jobhunter.util.SecurityUtil;
import com.turkraft.springfilter.boot.Filter;
import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class ResumeController {
    @Autowired
    ResumeService resumeService;
    @Autowired
    UserService userService;
    @Autowired
    FilterBuilder filterBuilder;
    @Autowired
    FilterSpecificationConverter filterSpecificationConverter;
    @PostMapping("/resumes")
    public ResponseEntity<ResResumeCreateDTO> createResume(@Valid @RequestBody Resume req) throws IdInvalidException {
        return ResponseEntity.ok(resumeService.handlCreateResume(req));
    }
    @PutMapping("/resumes")
    public ResponseEntity<ResUpdateResumeDTO> updateResume(@RequestBody Resume req) throws IdInvalidException{
        return ResponseEntity.ok(resumeService.handlUpdateResume(req));
    }

    @DeleteMapping("/resumes/{Id}")
    public ResponseEntity<Void> deleteResume(@PathVariable("Id") Long id) throws IdInvalidException{
        resumeService.handlDeleteResume(id);
        return ResponseEntity.ok(null);
    }
    @GetMapping("/resumes/{Id}")
    public ResponseEntity<ResResumeDTO> getResumeById(@PathVariable("Id") Long id) throws IdInvalidException{
        return ResponseEntity.ok(resumeService.handlGetResumeById(id));
    }
    @GetMapping("/resumes")
    public ResponseEntity<ResultPaginationDTO> getAllResume(@Filter Specification<Resume> spec, Pageable pageable){
        List<Long> arrJobIds = null;
        String email = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        User currentUser = this.userService.handleGetUserByUsername(email);
        if (currentUser != null) {
            Company userCompany = currentUser.getCompany();
            if (userCompany != null) {
                List<Job> companyJobs = userCompany.getJobs();
                if (companyJobs != null && companyJobs.size() > 0) {
                    arrJobIds = companyJobs.stream().map(x -> x.getId())
                            .collect(Collectors.toList());
                }
            }
        }

        Specification<Resume> jobInSpec = filterSpecificationConverter.convert(filterBuilder.field("job")
                .in(filterBuilder.input(arrJobIds)).get());

        Specification<Resume> finalSpec = jobInSpec.and(spec);

        return ResponseEntity.ok(resumeService.handlGetAllResume(spec,pageable));
    }

    @PostMapping("/resumes/by-user")
    public ResponseEntity<ResultPaginationDTO> gelResumeByUser(Pageable pageable){
        return ResponseEntity.ok(resumeService.handlGetResumeByUser(pageable));
    }

}

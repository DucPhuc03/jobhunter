package com.example.jobhunter.controller;

import com.example.jobhunter.dto.response.ResCreateJobDTO;
import com.example.jobhunter.dto.response.ResultPaginationDTO;
import com.example.jobhunter.entity.Job;
import com.example.jobhunter.exception.IdInvalidException;
import com.example.jobhunter.service.JobService;
import com.turkraft.springfilter.boot.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")

public class JobController {

    @Autowired
    JobService jobService;
    @PostMapping("/jobs")
    public ResponseEntity<ResCreateJobDTO> createJob(@RequestBody Job req){
        return ResponseEntity.ok(jobService.handlCreateJob(req));

    }
    @PutMapping("/jobs")
    public ResponseEntity<ResCreateJobDTO> updateJob(@RequestBody Job req) throws IdInvalidException {
        return ResponseEntity.ok(jobService.handlUpdateJob(req));
    }

    @GetMapping("/jobs")
    public ResponseEntity<ResultPaginationDTO> getAllJob(@Filter Specification<Job> spec, Pageable pageable){
        return ResponseEntity.ok(jobService.handlGetAllJob(spec,pageable));
    }

    @GetMapping("/jobs/{Id}")
    public ResponseEntity<Job> getJobById(@PathVariable("Id") Long id) throws IdInvalidException{
        return ResponseEntity.ok(jobService.handlGetJobById(id));
    }
    @DeleteMapping("/jobs/{Id}")
    public ResponseEntity<Void> deleteJob(@PathVariable("Id") Long id) throws IdInvalidException{
        jobService.handlDeleteJob(id);
        return ResponseEntity.ok(null);
    }
}

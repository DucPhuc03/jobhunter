package com.example.jobhunter.controller;

import com.example.jobhunter.dto.response.ResultPaginationDTO;
import com.example.jobhunter.entity.Company;
import com.example.jobhunter.exception.IdInvalidException;
import com.example.jobhunter.service.CompanyService;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class CompanyController {
    @Autowired
    CompanyService companyService;
    @PostMapping("/companies")
    public ResponseEntity<Company> createCompany( @Valid @RequestBody Company company){
        return ResponseEntity.status(HttpStatus.CREATED).body(companyService.handleCreateCompany(company));

    }

    @GetMapping("/companies")
    public ResponseEntity<ResultPaginationDTO> getallCompany(
            @Filter Specification<Company> spec,
            Pageable pageable
    ){
       return ResponseEntity.status(HttpStatus.OK).body(companyService.handleGetAllCompany(spec,pageable));
    }

    @GetMapping("/companies/{Id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable("Id") Long id) throws IdInvalidException {
        return ResponseEntity.ok(companyService.handlGetByIdCompany(id));
    }
    @DeleteMapping("/companies/{Id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable("Id") Long id){
        companyService.handleDeleteCompany(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
    @PutMapping("/companies")
    public ResponseEntity<Company> updateCompany(@Valid @RequestBody Company req){
        return ResponseEntity.status(HttpStatus.OK).body(companyService.handleUpdateCompany(req));
    }
}

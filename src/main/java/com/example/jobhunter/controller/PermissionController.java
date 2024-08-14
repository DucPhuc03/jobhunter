package com.example.jobhunter.controller;

import com.example.jobhunter.dto.response.ResultPaginationDTO;
import com.example.jobhunter.entity.Permission;
import com.example.jobhunter.exception.IdInvalidException;
import com.example.jobhunter.service.PermissionService;
import com.turkraft.springfilter.boot.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class PermissionController {
    @Autowired
    PermissionService permissionService;
    @PostMapping("/permissions")
    public ResponseEntity<Permission> createPermission(@RequestBody Permission req) throws IdInvalidException {
        return ResponseEntity.ok(permissionService.handlCreatePermission(req));
    }
    @PutMapping("/permissions")
    public ResponseEntity<Permission> updatePermission(@RequestBody Permission req) throws IdInvalidException{
        return ResponseEntity.ok(permissionService.handlUpdatePermission(req));
    }
    @GetMapping("/permissions")
    public ResponseEntity<ResultPaginationDTO> getallPermission(@Filter Specification<Permission> spec, Pageable pageable){
        return ResponseEntity.ok(permissionService.handlGetAllPermission(spec,pageable));
    }
}

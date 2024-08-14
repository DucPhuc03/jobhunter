package com.example.jobhunter.controller;

import com.example.jobhunter.dto.response.ResCreateJobDTO;
import com.example.jobhunter.dto.response.ResultPaginationDTO;
import com.example.jobhunter.entity.Role;
import com.example.jobhunter.exception.IdInvalidException;
import com.example.jobhunter.service.RoleService;
import com.turkraft.springfilter.boot.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class RoleController {
    @Autowired
    RoleService roleService;
    @PostMapping("/roles")
    public ResponseEntity<Role> createRole(@RequestBody Role req) throws IdInvalidException {
        return ResponseEntity.ok(roleService.handlCreateRole(req));
    }
    @PutMapping("/roles")
    public ResponseEntity<Role> updateRole(@RequestBody Role req) throws IdInvalidException{
        return ResponseEntity.ok(roleService.handlUpdateRole(req));
    }
    @DeleteMapping("/roles/{Id}")
    public ResponseEntity<Void> deleteRole(@PathVariable("Id") Long id) throws IdInvalidException{
        roleService.handlDeleteRole(id);
        return ResponseEntity.ok(null);
    }
    @GetMapping("/roles")
    public ResponseEntity<ResultPaginationDTO> getAllRole(@Filter Specification<Role> spec, Pageable pageable){
        return ResponseEntity.ok(roleService.handlGetAllRole(spec, pageable));
    }
    @GetMapping("/roles/{Id}")
    public ResponseEntity<Role> getRoleById(@PathVariable("Id") Long id) throws IdInvalidException{
        return ResponseEntity.ok(roleService.handlGetRoleById(id));
    }

}

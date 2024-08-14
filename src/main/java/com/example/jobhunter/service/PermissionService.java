package com.example.jobhunter.service;

import com.example.jobhunter.dto.MetaDTO;
import com.example.jobhunter.dto.response.ResultPaginationDTO;
import com.example.jobhunter.entity.Permission;
import com.example.jobhunter.exception.IdInvalidException;
import com.example.jobhunter.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PermissionService {
    @Autowired
    PermissionRepository permissionRepository;
    public Permission handlCreatePermission(Permission permission) throws IdInvalidException {
        Boolean check= permissionRepository.existsByApiPathAndMethodAndModule(permission.getApiPath(), permission.getMethod(), permission.getModule());
        if(check){
            throw new IdInvalidException("Permisson da ton tai");
        }
        return permissionRepository.save(permission);
    }
    public Permission handlUpdatePermission(Permission permission) throws IdInvalidException{
        Optional<Permission> permissionOptional= permissionRepository.findById(permission.getId());
        if(permissionOptional.isEmpty()){
            throw new IdInvalidException("Khong ton tai permission");
        }
//        Boolean check= permissionRepository.existsByApiPathAndMethodAndModule(permission.getApiPath(), permission.getMethod(), permission.getModule());
//        if(check){
//            throw new IdInvalidException("Permisson da ton tai");
//        }
        Permission permissionDB=permissionOptional.get();
        permissionDB.setName(permission.getName());
        permissionDB.setMethod(permission.getMethod());
        permissionDB.setApiPath(permission.getApiPath());
        permissionDB.setModule(permission.getModule());
       return permissionRepository.save(permissionDB);
    }
    public ResultPaginationDTO handlGetAllPermission(Specification<Permission> spec, Pageable pageable){
        Page<Permission> permissionPage= permissionRepository.findAll(spec,pageable);
        ResultPaginationDTO rs=new ResultPaginationDTO();
        MetaDTO mt=new MetaDTO();
        mt.setPage(pageable.getPageNumber()+1);
        mt.setPages(permissionPage.getTotalPages());
        mt.setPageSize(pageable.getPageSize());
        mt.setTotal(permissionPage.getTotalElements());
        rs.setMeta(mt);
        rs.setResult(permissionPage.getContent());
        return rs;
    }
}

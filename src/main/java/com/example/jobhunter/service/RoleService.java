package com.example.jobhunter.service;

import com.example.jobhunter.dto.MetaDTO;
import com.example.jobhunter.dto.response.ResultPaginationDTO;
import com.example.jobhunter.entity.Permission;
import com.example.jobhunter.entity.Resume;
import com.example.jobhunter.entity.Role;
import com.example.jobhunter.exception.IdInvalidException;
import com.example.jobhunter.repository.PermissionRepository;
import com.example.jobhunter.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PermissionRepository permissionRepository;
    public Role handlCreateRole(Role role) throws IdInvalidException {
        Boolean checkName=roleRepository.existsByName(role.getName());
        if(checkName){
            throw new IdInvalidException("role da ton tai");
        }
        List<Long> listId= role.getPermissions().stream().map(x->x.getId()).toList();
        List<Permission> listPermission=permissionRepository.findByIdIn(listId);
        role.setPermissions(listPermission);
        return roleRepository.save(role);
    }
    public Role handlUpdateRole(Role role) throws IdInvalidException{
        Optional<Role> roleOptional= roleRepository.findById(role.getId());
        if(roleOptional.isEmpty()){
            throw new IdInvalidException("Role khong ton tai");
        }
        Role roleDB=roleOptional.get();
        roleDB.setName(role.getName());
        roleDB.setActive(role.getActive());
        roleDB.setDescription(role.getDescription());
        if(role.getPermissions()!=null){
            List<Long> listId= role.getPermissions().stream().map(x->x.getId()).toList();
            List<Permission> listPermission=permissionRepository.findByIdIn(listId);
            roleDB.setPermissions(listPermission);
        }
        return roleRepository.save(roleDB);
    }
    public void handlDeleteRole(Long id) throws IdInvalidException{
        Boolean checkRole=roleRepository.existsById(id);
        if(!checkRole){
            throw new IdInvalidException("khong ton tai role");
        }
        roleRepository.deleteById(id);
    }
    public ResultPaginationDTO handlGetAllRole(Specification<Role> spec, Pageable pageable){
        Page<Role> rolePage=roleRepository.findAll(spec,pageable);
        ResultPaginationDTO rs=new ResultPaginationDTO();
        MetaDTO mt=new MetaDTO();
        mt.setPage(pageable.getPageNumber()+1);
        mt.setPages(rolePage.getTotalPages());
        mt.setPageSize(pageable.getPageSize());
        mt.setTotal(rolePage.getTotalElements());
        rs.setMeta(mt);
        rs.setResult(rolePage.getContent());
        return rs;
    }
    public Role handlGetRoleById(Long id) throws IdInvalidException{
        Optional<Role> currentRole=roleRepository.findById(id);
        if(currentRole.isEmpty()){
            throw new IdInvalidException("role khong ton tai");
        }
        return currentRole.get();
    }
}

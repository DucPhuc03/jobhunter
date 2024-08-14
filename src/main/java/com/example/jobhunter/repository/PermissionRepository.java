package com.example.jobhunter.repository;

import com.example.jobhunter.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<Permission,Long>, JpaSpecificationExecutor<Permission> {
    Boolean existsByApiPathAndMethodAndModule(String apiPath,String method, String module);
    List<Permission> findByIdIn(List<Long> id);
}

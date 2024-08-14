package com.example.jobhunter.repository;

import com.example.jobhunter.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long>, JpaSpecificationExecutor<Role> {
    Boolean existsByName(String name);
    Role findByName(String name);
}

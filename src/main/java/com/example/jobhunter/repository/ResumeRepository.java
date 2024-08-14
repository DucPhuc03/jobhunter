package com.example.jobhunter.repository;

import com.example.jobhunter.entity.Permission;
import com.example.jobhunter.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResumeRepository extends JpaRepository<Resume,Long>, JpaSpecificationExecutor<Resume> {

}

package com.example.jobhunter.repository;

import com.example.jobhunter.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface SkillRepository extends JpaRepository<Skill,Long>, JpaSpecificationExecutor<Skill> {
    Boolean existsByName(String name);
    List<Skill> findByIdIn(Collection<Long> id);
}

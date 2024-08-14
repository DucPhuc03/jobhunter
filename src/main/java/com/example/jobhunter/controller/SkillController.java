package com.example.jobhunter.controller;

import com.example.jobhunter.dto.response.ResultPaginationDTO;
import com.example.jobhunter.entity.Skill;
import com.example.jobhunter.exception.IdInvalidException;
import com.example.jobhunter.service.SkillService;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/v1")
public class SkillController {
    @Autowired
    SkillService skillService;
    @PostMapping("/skills")
    public ResponseEntity<Skill> createSkill(@Valid  @RequestBody Skill req) throws IdInvalidException {
        Skill res = skillService.hanldeCreateSkill(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping("/skills")
    public ResponseEntity<Skill> updateSkill(@RequestBody Skill req) throws IdInvalidException{
        Skill res=skillService.handlUpdateSkill(req);
        return ResponseEntity.ok(res);
    }
    @GetMapping("/skills")
    public ResponseEntity<ResultPaginationDTO> getSkill(@Filter Specification<Skill> spec, Pageable pageable){
        return ResponseEntity.ok(skillService.handlGetSkill(spec,pageable));
    }

}

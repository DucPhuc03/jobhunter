package com.example.jobhunter.service;

import com.example.jobhunter.dto.MetaDTO;
import com.example.jobhunter.dto.response.ResultPaginationDTO;
import com.example.jobhunter.entity.Skill;
import com.example.jobhunter.exception.IdInvalidException;
import com.example.jobhunter.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SkillService {
    @Autowired
    SkillRepository skillRepository;
    public Skill hanldeCreateSkill(Skill req) throws IdInvalidException {
        Boolean currentName=skillRepository.existsByName(req.getName());
        if(currentName){
            throw new IdInvalidException("Skill da ton tai");
        }
        skillRepository.save(req);
        return req;
    }
    public Skill handlUpdateSkill(Skill req) throws IdInvalidException{
        Optional<Skill> currentSkill=skillRepository.findById(req.getId());
        if(currentSkill.isEmpty()){
            throw new IdInvalidException("Skill khong tont tai");
        }
        if(req.getName()!=null && skillRepository.existsByName(req.getName())){
            throw new IdInvalidException("Skill da ton tai");
        }
        currentSkill.get().setName(req.getName());
        skillRepository.save(currentSkill.get());
        return currentSkill.get();
    }
    public ResultPaginationDTO handlGetSkill(Specification<Skill> spec, Pageable pageable){
        Page<Skill> pageSkill =skillRepository.findAll(spec,pageable);
        ResultPaginationDTO res=new ResultPaginationDTO();
        MetaDTO mt=new MetaDTO();
        mt.setPage(pageable.getPageNumber()+1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageSkill.getTotalPages());
        mt.setTotal(pageSkill.getTotalElements());
        res.setMeta(mt);
        res.setResult(pageSkill.getContent());
        return res;
    }
}

package com.example.jobhunter.service;

import com.example.jobhunter.dto.MetaDTO;
import com.example.jobhunter.dto.response.ResCreateJobDTO;
import com.example.jobhunter.dto.response.ResultPaginationDTO;
import com.example.jobhunter.entity.Job;
import com.example.jobhunter.entity.Skill;
import com.example.jobhunter.exception.IdInvalidException;
import com.example.jobhunter.repository.JobRepository;
import com.example.jobhunter.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobService {
    @Autowired
    JobRepository jobRepository;
    @Autowired
    SkillRepository skillRepository;
    public ResCreateJobDTO handlCreateJob(Job job){
        if(job.getSkills()!=null){

            List<Long> currentSkills=job.getSkills().stream().map((x->x.getId())).collect(Collectors.toList());
            List<Skill> dbSkils=skillRepository.findByIdIn(currentSkills);
            job.setSkills(dbSkils);
        }
        Job currentJob =jobRepository.save(job);
        ResCreateJobDTO res=new ResCreateJobDTO();
        res.setId(currentJob.getId());
        res.setName(currentJob.getName());
        res.setLocation(currentJob.getLocation());
        res.setSalary(currentJob.getSalary());
        res.setQuantity(currentJob.getQuantity());
        res.setDescription(currentJob.getDescription());
        res.setEndDate(currentJob.getEndDate());
        res.setStartDate(currentJob.getStartDate());
        res.setLevel(currentJob.getLevel());
        res.setActive(currentJob.isActive());
        res.setCreatedBy(currentJob.getCreatedBy());
        res.setCreatedAt(currentJob.getCreatedAt());
        res.setUpdatedBy(currentJob.getUpdatedBy());
        res.setUpdatedAt(currentJob.getUpdatedAt());
        List<String> skils=currentJob.getSkills().stream().map(x->x.getName()).collect(Collectors.toList());
        res.setSkills(skils);
        return res;
    }

    public ResCreateJobDTO handlUpdateJob(Job job) throws IdInvalidException {
        Optional<Job> jobOptional=jobRepository.findById(job.getId());
        Job jobInDB=jobOptional.get();
        if(jobOptional.isEmpty()){
            throw new IdInvalidException("Job khong ton tai");
        }
        if(job.getSkills()!=null){
            List<Long> currentSkills=job.getSkills().stream().map((x->x.getId())).collect(Collectors.toList());
            List<Skill> dbSkils=skillRepository.findByIdIn(currentSkills);
            jobInDB.setSkills(dbSkils);
        }
        jobInDB.setName(job.getName());
        jobInDB.setSalary(job.getSalary());
        jobInDB.setActive(job.isActive());
        jobInDB.setQuantity(job.getQuantity());
        jobInDB.setLevel(job.getLevel());
        jobInDB.setLocation(job.getLocation());
        jobInDB.setEndDate(job.getEndDate());
        jobInDB.setStartDate(job.getStartDate());

        Job currentJob =jobRepository.save(jobInDB);


        ResCreateJobDTO res=new ResCreateJobDTO();
        res.setId(currentJob.getId());
        res.setName(currentJob.getName());
        res.setLocation(currentJob.getLocation());
        res.setSalary(currentJob.getSalary());
        res.setQuantity(currentJob.getQuantity());
        res.setDescription(currentJob.getDescription());
        res.setEndDate(currentJob.getEndDate());
        res.setStartDate(currentJob.getStartDate());
        res.setLevel(currentJob.getLevel());
        res.setActive(currentJob.isActive());
        res.setCreatedBy(currentJob.getCreatedBy());
        res.setCreatedAt(currentJob.getCreatedAt());
        res.setUpdatedBy(currentJob.getUpdatedBy());
        res.setUpdatedAt(currentJob.getUpdatedAt());
        List<String> skils=currentJob.getSkills().stream().map(x->x.getName()).collect(Collectors.toList());
        res.setSkills(skils);
        return res;
    }
    public ResultPaginationDTO handlGetAllJob(Specification<Job> spec, Pageable pageable){
        Page<Job> jobPage=jobRepository.findAll(spec,pageable);
        ResultPaginationDTO rs=new ResultPaginationDTO();
        MetaDTO mt=new MetaDTO();
        mt.setPage(pageable.getPageNumber()+1);
        mt.setPages(jobPage.getTotalPages());
        mt.setPageSize(pageable.getPageSize());
        mt.setTotal(jobPage.getTotalElements());
        rs.setMeta(mt);
        rs.setResult(jobPage.getContent());
        return rs;
    }
    public Job handlGetJobById(Long id) throws IdInvalidException{
        Optional<Job> jobOptional=jobRepository.findById(id);
        if(jobOptional.isEmpty()){
            throw new IdInvalidException("Job khong ton tai");
        }
        return jobOptional.get();
    }
    public void handlDeleteJob(Long id) throws IdInvalidException{
        Optional<Job> jobOptional=jobRepository.findById(id);
        if(jobOptional.isEmpty()){
            throw new IdInvalidException("Job Khong ton tai");
        }
        jobRepository.deleteById(id);
    }
}

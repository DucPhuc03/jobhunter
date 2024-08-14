package com.example.jobhunter.service;

import com.example.jobhunter.dto.MetaDTO;
import com.example.jobhunter.dto.response.ResResumeCreateDTO;
import com.example.jobhunter.dto.response.ResResumeDTO;
import com.example.jobhunter.dto.response.ResUpdateResumeDTO;
import com.example.jobhunter.dto.response.ResultPaginationDTO;
import com.example.jobhunter.entity.Resume;
import com.example.jobhunter.exception.IdInvalidException;
import com.example.jobhunter.repository.JobRepository;
import com.example.jobhunter.repository.ResumeRepository;
import com.example.jobhunter.repository.UserRepository;
import com.example.jobhunter.util.SecurityUtil;
import com.turkraft.springfilter.converter.FilterSpecification;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import com.turkraft.springfilter.parser.FilterParser;
import com.turkraft.springfilter.parser.node.FilterNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.swing.text.html.parser.Entity;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ResumeService {
    @Autowired
    ResumeRepository resumeRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JobRepository jobRepository;
    @Autowired
    private FilterParser filterParser;

    @Autowired
    private FilterSpecificationConverter filterSpecificationConverter;

    public ResResumeCreateDTO handlCreateResume(Resume resume) throws IdInvalidException {
        Boolean checkUser=userRepository.existsById(resume.getUser().getId());
        Boolean checkJob=jobRepository.existsById(resume.getJob().getId());
        if(!checkUser || !checkJob){
            throw new IdInvalidException("Khong ton tai user hoac job");
        }
        Resume rs= resumeRepository.save(resume);
        ResResumeCreateDTO res=new ResResumeCreateDTO();
        res.setId(rs.getId());
        res.setCreatedAt(rs.getCreatedAt());
        res.setCreatedBy(rs.getCreatedBy());
        return res;
    }

    public ResUpdateResumeDTO handlUpdateResume(Resume resume) throws IdInvalidException{
        Optional<Resume> checkResume=resumeRepository.findById(resume.getId());
        if(checkResume.isEmpty()){
            throw new IdInvalidException("khong co CV");
        }
        checkResume.get().setStatus(resume.getStatus());
        resumeRepository.save(checkResume.get());
        ResUpdateResumeDTO res=new ResUpdateResumeDTO();
        res.setUpdatedAt(checkResume.get().getUpdatedAt());
        res.setUpdatedBy(checkResume.get().getUpdatedBy());
        return res;
    }
    public void handlDeleteResume(Long id) throws IdInvalidException{
        Boolean checkResume=resumeRepository.existsById(id);
        if(!checkResume){
            throw new IdInvalidException("khong tai tai CV");
        }
        resumeRepository.deleteById(id);
    }

    public ResResumeDTO handlGetResumeById(Long id) throws IdInvalidException{
        Optional<Resume> optionalResume=resumeRepository.findById(id);
        if(optionalResume.isEmpty()){
            throw new IdInvalidException("CV khong ton tai");
        }
        Resume resume=optionalResume.get();
        ResResumeDTO res=new ResResumeDTO();
        res.setId(resume.getId());
        res.setEmail(resume.getEmail());
        res.setUrl(resume.getUrl());
        res.setStatus(resume.getStatus());
        res.setCreatedBy(resume.getCreatedBy());
        res.setCreatedAt(resume.getCreatedAt());
        res.setUpdatedAt(resume.getUpdatedAt());
        res.setUpdatedBy(resume.getUpdatedBy());
        if(resume.getJob()!=null){
            res.setCompanyName(resume.getJob().getCompany().getName());
        }

        res.setUser(new ResResumeDTO.UserResume(resume.getUser().getId(),resume.getUser().getName()));
        res.setJob(new ResResumeDTO.JobResume(resume.getJob().getId(),resume.getJob().getName()));
        return res;
    }

    public ResultPaginationDTO handlGetAllResume(Specification<Resume> spec, Pageable pageable){
        Page<Resume> resumePage=resumeRepository.findAll(spec,pageable);
        ResultPaginationDTO res=new ResultPaginationDTO();
        MetaDTO mt=new MetaDTO();
        mt.setPage(pageable.getPageNumber()+1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(resumePage.getTotalPages());
        mt.setTotal(resumePage.getTotalElements());
        res.setMeta(mt);
        List<ResResumeDTO> resume=resumePage.getContent().stream().map(item->new ResResumeDTO(
                item.getId(),
                item.getEmail(),
                item.getUrl(),
                item.getStatus(),
                item.getCreatedAt(),
                item.getUpdatedAt(),
                item.getCreatedBy(),
                item.getUpdatedBy(),
                item.getJob().getCompany().getName(),
                new ResResumeDTO.UserResume(item.getUser().getId(),item.getUser().getName()),
                new ResResumeDTO.JobResume(item.getJob().getId(),item.getJob().getName())
        )).toList();
        res.setResult(resume);
        return res;
    }

    public ResultPaginationDTO handlGetResumeByUser(Pageable pageable){
        String email= SecurityUtil.getCurrentUserLogin().isPresent()==true?SecurityUtil.getCurrentUserLogin().get() : "";
        FilterNode node = filterParser.parse("email='"+ email+ "'");
        FilterSpecification<Resume> spec = filterSpecificationConverter.convert(node);
        Page<Resume> resumePage=resumeRepository.findAll(spec,pageable);
        ResultPaginationDTO res=new ResultPaginationDTO();
        MetaDTO mt=new MetaDTO();
        mt.setPage(pageable.getPageNumber()+1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(resumePage.getTotalPages());
        mt.setTotal(resumePage.getTotalElements());
        res.setMeta(mt);
        List<ResResumeDTO> resume=resumePage.getContent().stream().map(item->new ResResumeDTO(
                item.getId(),
                item.getEmail(),
                item.getUrl(),
                item.getStatus(),
                item.getCreatedAt(),
                item.getUpdatedAt(),
                item.getCreatedBy(),
                item.getUpdatedBy(),
                item.getJob().getCompany().getName(),
                new ResResumeDTO.UserResume(item.getUser().getId(),item.getUser().getName()),
                new ResResumeDTO.JobResume(item.getJob().getId(),item.getJob().getName())
        )).toList();
        res.setResult(resume);
        return res;
    }
}

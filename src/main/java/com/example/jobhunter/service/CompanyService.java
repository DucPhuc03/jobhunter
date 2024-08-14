package com.example.jobhunter.service;

import com.example.jobhunter.dto.MetaDTO;
import com.example.jobhunter.dto.response.ResultPaginationDTO;
import com.example.jobhunter.entity.Company;
import com.example.jobhunter.entity.User;
import com.example.jobhunter.exception.IdInvalidException;
import com.example.jobhunter.repository.CompanyRepository;
import com.example.jobhunter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    UserRepository userRepository;
    public Company handleCreateCompany(Company company){
         return companyRepository.save(company);

    }
    public ResultPaginationDTO handleGetAllCompany(Specification<Company> spec,Pageable pageable){
        Page<Company> pageCompany= companyRepository.findAll(spec,pageable);
        ResultPaginationDTO rs=new ResultPaginationDTO();
        MetaDTO mt=new MetaDTO();
        mt.setPage(pageable.getPageNumber()+1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageCompany.getTotalPages());
        mt.setTotal(pageCompany.getTotalElements());
        rs.setMeta(mt);
        rs.setResult(pageCompany.getContent());
        return rs;
    }

    public Company handlGetByIdCompany(Long id) throws IdInvalidException {
        Optional<Company> optionalCompany=companyRepository.findById(id);
        if(optionalCompany.isEmpty()){
            throw new IdInvalidException("cong ty khong ton tai");
        }
        return optionalCompany.get();
    }
    public void handleDeleteCompany(Long id){
        Optional<Company> companyOptional=companyRepository.findById(id);
        if(companyOptional.isPresent()){
            List<User> users= userRepository.findByCompany(companyOptional.get());
            userRepository.deleteAll(users);
        }
        companyRepository.deleteById(id);
    }
    public Company handleUpdateCompany(Company company){
        Optional<Company> companyOptional=companyRepository.findById(company.getId());
        if(companyOptional.isPresent()){
            Company companyCurrent=companyOptional.get();
            companyCurrent.setLogo(company.getLogo());
            companyCurrent.setName(company.getName());
            companyCurrent.setAddress(company.getAddress());
            companyCurrent.setDescription(company.getDescription());
            return companyRepository.save(companyCurrent);
        }
        else return null;
    }
}

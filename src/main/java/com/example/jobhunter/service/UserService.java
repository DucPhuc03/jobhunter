package com.example.jobhunter.service;

import com.example.jobhunter.dto.*;
import com.example.jobhunter.dto.response.ResCreateUserDTO;
import com.example.jobhunter.dto.response.ResUserDTO;
import com.example.jobhunter.dto.response.ResUserUpdateDTO;
import com.example.jobhunter.dto.response.ResultPaginationDTO;
import com.example.jobhunter.entity.Company;
import com.example.jobhunter.entity.User;
import com.example.jobhunter.exception.IdInvalidException;
import com.example.jobhunter.repository.CompanyRepository;
import com.example.jobhunter.repository.RoleRepository;
import com.example.jobhunter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    RoleRepository roleRepository;
    public void handlCreateUser(User user) throws IdInvalidException {
        if(user.getCompany() !=null){
            Optional<Company> companyOptional =companyRepository.findById(user.getCompany().getId());
            user.setCompany(companyOptional.orElse(null));
        }
//        Boolean checkRole=roleRepository.existsById(user.getRole().getId());
//        if(!checkRole){throw new IdInvalidException("role khong ton tai");
//        }

         userRepository.save(user);
    }
    public void handlDeleteUser(Long id){
        userRepository.deleteById(id);
    }
    public ResultPaginationDTO handlGetallUser(Specification<User> spec, Pageable pageable){
        Page<User> pageuser= userRepository.findAll(spec, pageable);
        List<ResUserDTO> listUser=pageuser.getContent().stream().map(item -> convertUsertoRespon(item)).toList();
        ResultPaginationDTO rs=new ResultPaginationDTO();
        MetaDTO mt=new MetaDTO();
        mt.setPage(pageable.getPageNumber()+1);
        mt.setPages(pageuser.getTotalPages());
        mt.setPageSize(pageable.getPageSize());
        mt.setTotal(pageuser.getTotalElements());
        rs.setMeta(mt);
        rs.setResult(listUser);
        return rs;
    }

    public User handleUpdateUser(User req) throws IdInvalidException{
        User user= userRepository.findById(req.getId()).orElseThrow();
        if(req.getCompany()!=null){
            Optional<Company> companyOptional =companyRepository.findById(req.getCompany().getId());
            user.setCompany(companyOptional.orElse(null));
        }
        Boolean checkRole=roleRepository.existsById(req.getRole().getId());
        if(!checkRole){
            throw new IdInvalidException("role khong ton tai");
        }
        user.setRole(req.getRole());
        user.setName(req.getName());
        user.setAddress(req.getAddress());
        user.setAge(req.getAge());
        user.setGender(req.getGender());
        userRepository.save(user);
        return user;
    }
    public User handlGetUserByid(Long id){
        return userRepository.findById(id).orElseThrow();
    }
    public User handleGetUserByUsername(String username){
        return userRepository.findByEmail(username);
    }
    public Optional<User> existsById(Long id){
        return userRepository.findById(id);
    }
    public Boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    public ResCreateUserDTO convertUsertoResponCreate (User user){
        ResCreateUserDTO res=new ResCreateUserDTO();
        ResCreateUserDTO.CompanyUser companyUser=new ResCreateUserDTO.CompanyUser();
        if(user.getCompany()!=null){
            companyUser.setId(user.getCompany().getId());
            companyUser.setName(user.getCompany().getName());
        }
        res.setId(user.getId());
        res.setName(user.getName());
        res.setEmail(user.getEmail());
        res.setAddress(user.getAddress());
        res.setAge(user.getAge());
        res.setGender(user.getGender());
        res.setCreatedAt(user.getCreatedAt());
        res.setCompany(companyUser);
        return res;
    }

    public ResUserDTO convertUsertoRespon(User user){
        ResUserDTO.CompanyUser companyUser=new ResUserDTO.CompanyUser();
        ResUserDTO.RoleUser roleUser=new ResUserDTO.RoleUser();
        if(user.getRole()!=null){
            roleUser.setId(user.getRole().getId());
            roleUser.setName(user.getRole().getName());
        }
        if(user.getCompany()!=null){
            companyUser.setId(user.getCompany().getId());
            companyUser.setName(user.getCompany().getName());
        }
        ResUserDTO res=new ResUserDTO();
        res.setId(user.getId());
        res.setName(user.getName());
        res.setEmail(user.getEmail());
        res.setAddress(user.getAddress());
        res.setAge(user.getAge());
        res.setGender(user.getGender());
        res.setCreatedBy(user.getCreatedBy());
        res.setUpdatedBy(user.getUpdatedBy());
        res.setCompany(companyUser);
        res.setRole(roleUser);
        return res;
    }

    public ResUserUpdateDTO convertUsertoUpdate(User user){
        ResUserUpdateDTO.CompanyUser companyUser=new ResUserUpdateDTO.CompanyUser();
        companyUser.setId(user.getCompany().getId());
        companyUser.setName(user.getCompany().getName());
        ResUserUpdateDTO res=new ResUserUpdateDTO();
        res.setId(user.getId());
        res.setName(user.getName());
        res.setAddress(user.getAddress());
        res.setAge(user.getAge());
        res.setGender(user.getGender());
        res.setUpdatedBy(user.getUpdatedBy());
        res.setCompany(companyUser);
        return res;
    }

    public void updateUserToken(String refreshToken, String email){
        User currentUser=this.handleGetUserByUsername(email);
        if(currentUser!=null){
            currentUser.setRefreshToken(refreshToken);
            userRepository.save(currentUser);
        }
    }
    public User getByRefreshTokenAndEmail(String token, String email){
        return userRepository.findByRefreshTokenAndEmail(token,email);
    }
}

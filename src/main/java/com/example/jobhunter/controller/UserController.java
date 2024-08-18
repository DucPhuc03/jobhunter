package com.example.jobhunter.controller;

import com.example.jobhunter.dto.response.ResCreateUserDTO;
import com.example.jobhunter.dto.response.ResUserDTO;
import com.example.jobhunter.dto.response.ResUserUpdateDTO;
import com.example.jobhunter.dto.response.ResultPaginationDTO;
import com.example.jobhunter.dto.response.RestResponse;
import com.example.jobhunter.entity.User;
import com.example.jobhunter.exception.IdInvalidException;
import com.example.jobhunter.service.UserService;
import com.turkraft.springfilter.boot.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public ResponseEntity<RestResponse<String>> home(){
        RestResponse<String> res=new RestResponse<String>();
        res.setData("hello0000");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
    @PostMapping("/users")
    public ResponseEntity<ResCreateUserDTO> createNewUser(@RequestBody User req) throws IdInvalidException{
        Boolean currentUser=userService.existsByEmail(req.getEmail());
        if(currentUser){
            throw new IdInvalidException("email da ton tai");
        }

        String hashPassword=passwordEncoder.encode(req.getPassword());
        User user=new User();
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setPassword(hashPassword);
        user.setAddress(req.getAddress());
        user.setGender(req.getGender());
        user.setAge(req.getAge());
        user.setCreatedAt(req.getCreatedAt());
        user.setCompany(req.getCompany());
        user.setRole(req.getRole());
        userService.handlCreateUser(user);
        ResCreateUserDTO res= userService.convertUsertoResponCreate(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }
    @DeleteMapping("/users/{Id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("Id") Long id) throws IdInvalidException{
        Optional<User> currentUser=userService.existsById(id);
        if(currentUser.isEmpty()){
            throw new IdInvalidException("user khong ton tai");
        }
        userService.handlDeleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
    @GetMapping("/users")
    public ResponseEntity<ResultPaginationDTO> getallUser(@Filter Specification<User> spec, Pageable pageable){

        return ResponseEntity.status(HttpStatus.OK).body(userService.handlGetallUser(spec,pageable));
    }
    @GetMapping("/users/{Id}")
    public ResponseEntity<ResUserDTO> getUserByid(@PathVariable("Id") Long id){
        User user= userService.handlGetUserByid(id);
        ResUserDTO res= userService.convertUsertoRespon(user);

        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @PutMapping("/users")
    public ResponseEntity<ResUserUpdateDTO> updateUser(@RequestBody User req) throws IdInvalidException{
        Optional<User> currentUser=userService.existsById(req.getId());
        if(currentUser.isEmpty()){
            throw new IdInvalidException("user khong ton tai");
        }
        User user=userService.handleUpdateUser(req);
        ResUserUpdateDTO res= userService.convertUsertoUpdate(user);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}

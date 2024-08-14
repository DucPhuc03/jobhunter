package com.example.jobhunter.controller;

import com.example.jobhunter.dto.request.ReqLoginDTO;
import com.example.jobhunter.dto.response.ResCreateUserDTO;
import com.example.jobhunter.dto.response.ResLoginDTO;
import com.example.jobhunter.entity.User;
import com.example.jobhunter.exception.IdInvalidException;
import com.example.jobhunter.service.UserService;
import com.example.jobhunter.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1")
public class AuthController {
    @Autowired
    UserService userService;
    @Autowired
    AuthenticationManagerBuilder authenticationManagerBuilder ;
    @Autowired
    SecurityUtil securityUtil;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Value("${com.example.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;
    @PostMapping("/auth/login")

    public ResponseEntity<ResLoginDTO> login(@RequestBody ReqLoginDTO reqLoginDTO){

        UsernamePasswordAuthenticationToken authenticationToken
                =new UsernamePasswordAuthenticationToken(reqLoginDTO.getUsername(), reqLoginDTO.getPassword());
        Authentication authentication= authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        //create token

        SecurityContextHolder.getContext().setAuthentication(authentication);
        ResLoginDTO res=new ResLoginDTO();
        User currentUserDB=userService.handleGetUserByUsername(reqLoginDTO.getUsername());
        ResLoginDTO.UserLogin user= new ResLoginDTO.UserLogin(currentUserDB.getId(),currentUserDB.getName(), currentUserDB.getEmail(),currentUserDB.getRole());
        String access_token=securityUtil.createToken(authentication.getName(),user);
        res.setAccessToken(access_token);
        res.setUser(user);
        String refreshToken= securityUtil.createRefreshToken(reqLoginDTO.getUsername(),user);
        userService.updateUserToken(refreshToken, reqLoginDTO.getUsername());

        ResponseCookie responseCookie=ResponseCookie.from("refresh_token",refreshToken)
                .httpOnly(true)
                .path("/")
                .secure(true)
                .maxAge(refreshTokenExpiration)
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,responseCookie.toString()).body(res);
    }

    @GetMapping("/auth/account")
    public ResponseEntity<ResLoginDTO.UserGetAccount> getAccount(){
        String email=SecurityUtil.getCurrentUserLogin().isPresent()? SecurityUtil.getCurrentUserLogin().get():"";
        User user =userService.handleGetUserByUsername(email);
        ResLoginDTO.UserLogin currentUser=new ResLoginDTO.UserLogin();
        ResLoginDTO.UserGetAccount userGetAccount=new ResLoginDTO.UserGetAccount();
        if(user!=null){
            currentUser.setId(user.getId());
            currentUser.setName(user.getName());
            currentUser.setEmail(user.getEmail());
            currentUser.setRole(user.getRole());
            userGetAccount.setUser(currentUser);
        }
        return ResponseEntity.ok(userGetAccount);
    }

    @GetMapping("/auth/refresh")
    public ResponseEntity<ResLoginDTO> getRefreshToken(@CookieValue(name = "refresh_token") String refresh_token) throws IdInvalidException {
        Jwt decodedToken= this.securityUtil.checkValidRefreshToken(refresh_token);
        String email=decodedToken.getSubject();
        User currentUserDB=userService.getByRefreshTokenAndEmail(refresh_token,email);
        if(currentUserDB==null){
            throw new IdInvalidException("User khong ton tai");
        }

        ResLoginDTO res=new ResLoginDTO();
        User currentUser=userService.handleGetUserByUsername(email);
        ResLoginDTO.UserLogin user= new ResLoginDTO.UserLogin(currentUser.getId(),currentUser.getName(), currentUser.getEmail(),currentUser.getRole());
        String access_token=securityUtil.createToken(email,user);
        res.setAccessToken(access_token);
        res.setUser(user);
        String refreshToken= securityUtil.createRefreshToken(email,user);
        userService.updateUserToken(refreshToken,email);

        ResponseCookie responseCookie=ResponseCookie.from("refresh_token",refreshToken)
                .httpOnly(true)
                .path("/")
                .secure(true)
                .maxAge(refreshTokenExpiration)
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,responseCookie.toString()).body(res);
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<Void> logout() throws IdInvalidException{
        String email=SecurityUtil.getCurrentUserLogin().isPresent()?SecurityUtil.getCurrentUserLogin().get() : "";
        if(email.isEmpty()){
            throw new IdInvalidException("khong ton tai user");
        }
        userService.updateUserToken(null,email);
        ResponseCookie deleteSpringcokie=ResponseCookie.from("refresh_token",null)
                .httpOnly(true)
                .path("/")
                .secure(true)
                .maxAge(0)
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,deleteSpringcokie.toString()).body(null);
    }
    @PostMapping("/auth/register")
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
        if(req.getCompany()!=null){
            user.setCompany(req.getCompany());
        }

        if(req.getRole()!=null){
            user.setRole(req.getRole());
        }
        userService.handlCreateUser(user);
        ResCreateUserDTO res= userService.convertUsertoResponCreate(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }
}



package com.example.jobhunter.config;

import com.example.jobhunter.entity.Permission;
import com.example.jobhunter.entity.Role;
import com.example.jobhunter.entity.User;
import com.example.jobhunter.exception.IdInvalidException;
import com.example.jobhunter.service.UserService;
import com.example.jobhunter.util.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.List;

public class PermissionInterceptor implements HandlerInterceptor {
    @Autowired
    UserService userService;
    @Override
    @Transactional
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response, Object handler)
            throws IdInvalidException {
        String path = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String requestURI = request.getRequestURI();
        String httpMethod = request.getMethod();
        System.out.println(">>> RUN preHandle");
        System.out.println(">>> path= " + path);
        System.out.println(">>> httpMethod= " + httpMethod);
        System.out.println(">>> requestURI= " + requestURI);

        String email=SecurityUtil.getCurrentUserLogin().isPresent()==true? SecurityUtil.getCurrentUserLogin().get() : "";
        if(!email.isEmpty()){
            User user=userService.handleGetUserByUsername(email);
            if(user!=null){
                Role role=user.getRole();
                if(role!=null){
                    List<Permission> permissions=role.getPermissions();
                    Boolean isAllow=permissions.stream().anyMatch(item->item.getApiPath().equals(path)&&item.getMethod().equals(httpMethod));
                    if(isAllow==false){
                        throw new IdInvalidException("ban khong co quyen");
                    }
                }
                else {
                    throw new IdInvalidException("ban khong co quyen");
                }
            }
        }
        return true;
    }
}

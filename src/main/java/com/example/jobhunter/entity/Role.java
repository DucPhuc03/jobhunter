package com.example.jobhunter.entity;

import com.example.jobhunter.util.SecurityUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "roles")
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String description;
    private Boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @OneToMany(mappedBy = "role",fetch = FetchType.LAZY)
    @JsonIgnore
    List<User> users;
    @JsonIgnoreProperties(value = { "roles" })
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "permission_role",joinColumns = @JoinColumn(name = "permission_id"),inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Permission> permissions;
    @PrePersist
    public void handleBeforeCreate(){
        this.createdBy= SecurityUtil.getCurrentUserLogin().isPresent()==true?SecurityUtil.getCurrentUserLogin().get() : "";

        this.createdAt=Instant.now();
    }
    @PreUpdate
    public void handleBeforeUpdate(){
        this.updatedBy= SecurityUtil.getCurrentUserLogin().isPresent()==true?SecurityUtil.getCurrentUserLogin().get() : "";
        this.updatedAt=Instant.now();
    }
}

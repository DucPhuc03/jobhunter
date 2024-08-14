package com.example.jobhunter.entity;

import com.example.jobhunter.util.Enum.GenderEnum;
import com.example.jobhunter.util.SecurityUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String name;
    private String email;
    private String password;
    private int age ;
    @Enumerated(EnumType.STRING)
    private GenderEnum gender;  // MALE/FEMALE;
    private String address ;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String refreshToken ;
    private Instant createdAt ;
    private Instant updatedAt ;
    private String createdBy ;
    private String updatedBy ;
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    @JsonIgnore
    List<Resume> resumes;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
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

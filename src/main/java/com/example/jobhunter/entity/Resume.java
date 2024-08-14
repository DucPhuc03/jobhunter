package com.example.jobhunter.entity;

import com.example.jobhunter.util.Enum.StatusEnum;
import com.example.jobhunter.util.SecurityUtil;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "resumes")
@Entity
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "email khong de trong")
    private String email;
    @NotBlank(message = "url khong de trong")
    private String url;
    @Enumerated(EnumType.STRING)
    private StatusEnum status;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

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

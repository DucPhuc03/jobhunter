package com.example.jobhunter.entity;

import com.example.jobhunter.util.SecurityUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
@Table(name = "skills")
@Entity
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "ten khong duoc de trong")
    private String name;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "skills")
    private List<Job> jobs;
    @ManyToMany(fetch = FetchType.LAZY,mappedBy = "skills")
    @JsonIgnore
    private List<Subscriber> subscribers;
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

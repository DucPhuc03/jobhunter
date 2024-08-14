package com.example.jobhunter.entity;

import com.example.jobhunter.util.SecurityUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "subscribers")
@Entity
public class Subscriber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String name;
    private Instant createdAt ;
    private Instant updatedAt ;
    private String createdBy ;
    private String updatedBy ;
    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "subscribers" })
    @JoinTable(name = "subscriber_skill", joinColumns = @JoinColumn(name = "subscriber_id"), inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private List<Skill> skills;
    @PrePersist
    public void handleBeforeCreate(){
        this.createdBy= SecurityUtil.getCurrentUserLogin().isPresent()==true?SecurityUtil.getCurrentUserLogin().get() : "";

        this.createdAt= Instant.now();
    }
    @PreUpdate
    public void handleBeforeUpdate(){
        this.updatedBy= SecurityUtil.getCurrentUserLogin().isPresent()==true?SecurityUtil.getCurrentUserLogin().get() : "";
        this.updatedAt=Instant.now();
    }
}

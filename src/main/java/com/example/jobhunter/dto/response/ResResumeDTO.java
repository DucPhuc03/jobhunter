package com.example.jobhunter.dto.response;

import com.example.jobhunter.util.Enum.StatusEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResResumeDTO {
    private long id;
    private String email;
    private String url;
    @Enumerated(EnumType.STRING)
    private StatusEnum status;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
    private String companyName;
    private UserResume user;
    private JobResume job;
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserResume{
        private long id;
        private String name;
    }
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class JobResume{
        private long id;
        private String name;
    }
}

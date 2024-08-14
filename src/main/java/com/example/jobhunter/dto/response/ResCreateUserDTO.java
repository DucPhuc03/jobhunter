package com.example.jobhunter.dto.response;

import com.example.jobhunter.util.Enum.GenderEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResCreateUserDTO {
    private Long Id;
    private String name;
    private String email;
    private int age ;
    @Enumerated(EnumType.STRING)
    private GenderEnum gender;  // MALE/FEMALE;
    private String address ;
    private CompanyUser company;
    private Instant createdAt ;
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CompanyUser{
        private long id;
        private String name;
    }
}

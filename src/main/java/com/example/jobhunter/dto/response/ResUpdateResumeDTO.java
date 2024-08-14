package com.example.jobhunter.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResUpdateResumeDTO {
        private Instant updatedAt;
        private String updatedBy;
}

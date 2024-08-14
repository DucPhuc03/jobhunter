package com.example.jobhunter.dto.response;

import com.example.jobhunter.dto.MetaDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultPaginationDTO {
    private MetaDTO meta;
    private Object result;
}

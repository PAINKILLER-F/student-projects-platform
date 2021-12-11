package com.platform.projapp.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;


import java.time.LocalDate;
import java.util.Base64;

@Data
public class AddOrUpdateSprintRequest {
    private Long sprintId;
    private Long projectId;
    private Integer number;
    private String goals;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate startDate;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate endDate;
    public Base64 presentation;
}

package com.tlaxcala.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsultListExamDTO {
    
    @NotNull
    private ConsultDTO consult;

    private List<ExamDTO> lstExam;

}

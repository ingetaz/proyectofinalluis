package com.tlaxcala.service;

import java.util.List;

import com.tlaxcala.model.ConsultExam;

public interface IConsultExamService {
    
    List<ConsultExam> getExamsByConsultId(Integer idConsult);
}

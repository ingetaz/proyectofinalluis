package com.tlaxcala.service.impl;

import org.springframework.stereotype.Service;

import com.tlaxcala.model.Exam;
import com.tlaxcala.repo.IGenericRepo;
import com.tlaxcala.repo.IExamRepo;
import com.tlaxcala.service.IExamService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExamServiceImpl extends CRUDImpl<Exam, Integer> implements IExamService {

    private final IExamRepo repo;

    @Override
    protected IGenericRepo<Exam, Integer> getRepo() {
        return repo;
    }

}

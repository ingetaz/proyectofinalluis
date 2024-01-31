package com.tlaxcala.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tlaxcala.model.Patient;
import com.tlaxcala.repo.IGenericRepo;
import com.tlaxcala.repo.IPatientRepo;
import com.tlaxcala.service.IPatientService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl extends CRUDImpl<Patient, Integer> implements IPatientService {

    private final IPatientRepo repo;

    @Override
    protected IGenericRepo<Patient, Integer> getRepo() {
        return repo;
    }

    @Override
    public Page<Patient> listPage(Pageable pageable) {
        return repo.findAll(pageable);
    }

}

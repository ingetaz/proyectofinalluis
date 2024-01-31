package com.tlaxcala.service.impl;

import org.springframework.stereotype.Service;

import com.tlaxcala.model.Specialty;
import com.tlaxcala.repo.IGenericRepo;
import com.tlaxcala.repo.ISpecialtyRepo;
import com.tlaxcala.service.ISpecialtyService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SpecialtyServiceImpl extends CRUDImpl<Specialty, Integer> implements ISpecialtyService {

    private final ISpecialtyRepo repo;

    @Override
    protected IGenericRepo<Specialty, Integer> getRepo() {
        return repo;
    }

}

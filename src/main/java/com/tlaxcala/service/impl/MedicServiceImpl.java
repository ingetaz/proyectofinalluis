package com.tlaxcala.service.impl;

import org.springframework.stereotype.Service;

import com.tlaxcala.model.Medic;
import com.tlaxcala.repo.IGenericRepo;
import com.tlaxcala.repo.IMedicRepo;
import com.tlaxcala.service.IMedicService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MedicServiceImpl extends CRUDImpl<Medic, Integer> implements IMedicService {

    private final IMedicRepo repo;

    @Override
    protected IGenericRepo<Medic, Integer> getRepo() {
        return repo;
    }

}


package com.tlaxcala.service.impl;

import org.springframework.stereotype.Service;

import com.tlaxcala.model.MediaFile;
import com.tlaxcala.repo.IGenericRepo;
import com.tlaxcala.repo.IMediaFileRepo;
import com.tlaxcala.service.IMediaFileService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MediaFileServiceImpl extends CRUDImpl<MediaFile, Integer> implements IMediaFileService {

    private final IMediaFileRepo repo;

    @Override
    protected IGenericRepo<MediaFile, Integer> getRepo() {
        return repo;
    }
    
}

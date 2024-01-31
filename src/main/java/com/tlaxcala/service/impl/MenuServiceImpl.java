package com.tlaxcala.service.impl;

import com.tlaxcala.repo.IGenericRepo;
import com.tlaxcala.repo.IMenuRepo;
import com.tlaxcala.service.IMenuService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.tlaxcala.model.Menu;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl extends CRUDImpl<Menu, Integer> implements IMenuService {

    private final IMenuRepo repo;

    @Override
    protected IGenericRepo<Menu, Integer> getRepo() {
        return repo;
    }

    @Override
    public List<Menu> getMenusByUsername(String username) {
        String contextSessionUser = SecurityContextHolder.getContext().getAuthentication().getName();
        return repo.getMenusByUsername(contextSessionUser);
    }
}

package com.tlaxcala.service;

import com.tlaxcala.model.Menu;

import java.util.List;

public interface IMenuService extends ICRUD<Menu, Integer> {

    List<Menu> getMenusByUsername(String username);

}

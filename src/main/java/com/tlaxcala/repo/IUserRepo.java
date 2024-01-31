package com.tlaxcala.repo;

import com.tlaxcala.model.User;

public interface IUserRepo extends IGenericRepo<User, Integer> {

    // derived queries
    User findOneByUsername(String username);
    
}

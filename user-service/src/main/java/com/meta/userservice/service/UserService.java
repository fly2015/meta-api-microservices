/*
 * UserService.java
 *
 * Copyright by Hien Ng
 * Da Nang
 * All rights reserved.
 */
package com.meta.userservice.service;

import com.meta.userservice.entity.User;

import java.util.List;
import java.util.Optional;


/**
 * 
 *
 * @author nhqhien
 * @version $Revision:  $
 */
public interface UserService
{
    Optional<User> getUserById(Long id);
    List<User> getAllUsers();
    User createUser(User user);
    User updateUser(User user);
    void deleteUser(Long userId);
    Optional<User> findByUsername(String username);
}



/*
 * Changes:
 * $Log: $
 */
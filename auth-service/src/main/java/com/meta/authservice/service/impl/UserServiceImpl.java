/*
 * UserServiceImpl.java
 *
 * Copyright by Hien Ng
 * Da Nang
 * All rights reserved.
 */
package com.meta.authservice.service.impl;

import com.meta.authservice.entity.User;
import com.meta.authservice.repository.UserRepository;
import com.meta.authservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 
 *
 * @author nhqhien
 * @version $Revision:  $
 */
@Service
public class UserServiceImpl implements UserService
{
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }


    public Optional<User> getUserById(Long id)
    {
        return userRepository.findById(id);
    }


    public List<User> getAllUsers()
    {
        return userRepository.findAll();
    }


    public User createUser(User user)
    {
        return userRepository.save(user);
    }


    public User updateUser(User user)
    {
        return userRepository.save(user);
    }


    public void deleteUser(Long userId)
    {
        userRepository.deleteById(userId);
    }


    public Optional<User> findByUsername(String username)
    {
        return userRepository.findByUsername(username);
    }
   
}



/*
 * Changes:
 * $Log: $
 */
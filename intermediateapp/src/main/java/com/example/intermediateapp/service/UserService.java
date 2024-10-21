package com.example.intermediateapp.service;


import com.example.intermediateapp.model.UserDto;
import com.example.intermediateapp.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public List<UserDto> findAll() {
        return userMapper.findAll();
    }
}
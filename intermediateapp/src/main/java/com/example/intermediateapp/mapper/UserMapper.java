package com.example.intermediateapp.mapper;

import com.example.intermediateapp.model.UserDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM users")
    List<UserDto> findAll();
}
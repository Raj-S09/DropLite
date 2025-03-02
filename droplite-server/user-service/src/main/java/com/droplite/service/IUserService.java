package com.droplite.service;

import com.droplite.dto.UserDto;

public interface IUserService {

    UserDto getByUsername(String username);
    UserDto createUser(UserDto userDto);

}

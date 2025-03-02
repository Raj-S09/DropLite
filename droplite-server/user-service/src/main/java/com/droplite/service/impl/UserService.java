package com.droplite.service.impl;

import com.droplite.constant.UserConstants;
import com.droplite.dto.UserDto;
import com.droplite.entity.User;
import com.droplite.exception.DropLiteException;
import com.droplite.repository.UserRepository;
import com.droplite.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;

    @Override
    public UserDto getByUsername(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty())
            throw new DropLiteException(UserConstants.ERROR_MSG_USER_NOT_FOUND);
        User user = userOpt.get();
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .name(user.getName())
                .role(user.getRole())
                .build();
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        Optional<User> userOpt = userRepository.findByUsername(userDto.getUsername());
        if (userOpt.isPresent())
            throw new DropLiteException(UserConstants.ERROR_MSG_USERNAME_TAKEN);
        User user = User.builder()
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .name(userDto.getName())
                .active(true)
                .role(UserConstants.ROLE_USER)
                .build();
        User savedUser = userRepository.save(user);
        userDto.setId(savedUser.getId());
        return userDto;
    }

}

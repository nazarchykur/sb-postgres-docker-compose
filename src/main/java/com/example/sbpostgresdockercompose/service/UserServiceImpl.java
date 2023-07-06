package com.example.sbpostgresdockercompose.service;

import com.example.sbpostgresdockercompose.dto.UserDtoRequest;
import com.example.sbpostgresdockercompose.dto.UserDtoResponse;
import com.example.sbpostgresdockercompose.entity.User;
import com.example.sbpostgresdockercompose.exception.EntityNotFoundException;
import com.example.sbpostgresdockercompose.repository.UserRepository;
import com.example.sbpostgresdockercompose.util.DtoMapperUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    public static final String USER_NOT_FOUND = "User not found with id= %d";

    private final UserRepository userRepository;
    private final DtoMapperUtil dtoMapperUtil;

    @Override
    public UserDtoResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(USER_NOT_FOUND, id)));
        return dtoMapperUtil.toDto(user, UserDtoResponse.class);
    }

    @Override
    public List<UserDtoResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> dtoMapperUtil.toDto(user, UserDtoResponse.class))
                .toList();
    }

    @Override
    public UserDtoResponse createUser(UserDtoRequest userDtoRequest) {
        User user = dtoMapperUtil.toEntity(userDtoRequest, User.class);
        User savedUser = userRepository.save(user);
        return dtoMapperUtil.toDto(savedUser, UserDtoResponse.class);
    }

    @Override
    public UserDtoResponse updateUser(Long id, UserDtoRequest userDtoRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(USER_NOT_FOUND, id)));
        user.setName(userDtoRequest.name());
        user.setEmail(userDtoRequest.email());
        return dtoMapperUtil.toDto(user, UserDtoResponse.class);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(USER_NOT_FOUND, id)));
        userRepository.delete(user);
    }
}

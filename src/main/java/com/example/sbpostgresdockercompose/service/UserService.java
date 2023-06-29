package com.example.sbpostgresdockercompose.service;



import com.example.sbpostgresdockercompose.dto.UserDtoRequest;
import com.example.sbpostgresdockercompose.dto.UserDtoResponse;

import java.util.List;

public interface UserService {
    UserDtoResponse getUserById(Long id);
    List<UserDtoResponse> getAllUsers();
    UserDtoResponse createUser(UserDtoRequest userDtoRequest);
    UserDtoResponse updateUser(Long id, UserDtoRequest userDtoRequest);
    void deleteUser(Long id);
}

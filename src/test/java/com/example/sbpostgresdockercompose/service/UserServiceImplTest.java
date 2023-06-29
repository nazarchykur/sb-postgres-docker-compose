package com.example.sbpostgresdockercompose.service;

import com.example.sbpostgresdockercompose.dto.UserDtoRequest;
import com.example.sbpostgresdockercompose.dto.UserDtoResponse;
import com.example.sbpostgresdockercompose.entity.User;
import com.example.sbpostgresdockercompose.exception.EntityNotFoundException;
import com.example.sbpostgresdockercompose.repository.UserRepository;
import com.example.sbpostgresdockercompose.util.DtoMapperUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private DtoMapperUtil dtoMapperUtil;

    @InjectMocks
    private UserServiceImpl userServiceUnderTest;

    @Test
    void getUserById_ExistingUser_ReturnsUserDtoResponse() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserDtoResponse expectedUserDtoResponse = new UserDtoResponse();
        when(dtoMapperUtil.toDto(user, UserDtoResponse.class)).thenReturn(expectedUserDtoResponse);

        // Act
        UserDtoResponse userDtoResponse = userServiceUnderTest.getUserById(userId);

        // Assert
        assertThat(userDtoResponse).isEqualTo(expectedUserDtoResponse);

        verify(userRepository, only()).findById(userId);
        verify(dtoMapperUtil, only()).toDto(user, UserDtoResponse.class);
    }

    @Test
    void getUserById_NonExistingUser_ThrowsEntityNotFoundException() {
        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userServiceUnderTest.getUserById(userId));

        verify(userRepository, only()).findById(userId);
        verifyNoMoreInteractions(dtoMapperUtil);
    }

    @Test
    void getAllUsers_ReturnsUserDtoResponseList() {
        // Arrange
        User user1 = new User();
        user1.setId(1L);

        User user2 = new User();
        user2.setId(2L);

        List<User> userList = Arrays.asList(user1, user2);
        when(userRepository.findAll()).thenReturn(userList);

        UserDtoResponse dtoResponse1 = new UserDtoResponse();
        UserDtoResponse dtoResponse2 = new UserDtoResponse();
        when(dtoMapperUtil.toDto(user1, UserDtoResponse.class)).thenReturn(dtoResponse1);
        when(dtoMapperUtil.toDto(user2, UserDtoResponse.class)).thenReturn(dtoResponse2);

        // Act
        List<UserDtoResponse> actualDtoResponseList = userServiceUnderTest.getAllUsers();

        // Assert
        assertThat(actualDtoResponseList)
                .isNotEmpty()
                .containsExactlyInAnyOrder(dtoResponse1, dtoResponse2)
                .hasSize(2);

        verify(userRepository, only()).findAll();
        verify(dtoMapperUtil, times(1)).toDto(user1, UserDtoResponse.class);
        verify(dtoMapperUtil, times(1)).toDto(user2, UserDtoResponse.class);
    }

    @Test
    void getAllUsers_NoUsers_ReturnsEmptyList() {
        // Arrange
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<UserDtoResponse> actualDtoResponseList = userServiceUnderTest.getAllUsers();

        // Assert
        assertThat(actualDtoResponseList).isEmpty();

        verify(userRepository, times(1)).findAll();
        verifyNoMoreInteractions(dtoMapperUtil);
    }

    @Test
    void createUser_ValidUserDtoRequest_ReturnsUserDtoResponse() {
        // Arrange
        UserDtoRequest userDtoRequest = new UserDtoRequest();
        userDtoRequest.setName("John");
        userDtoRequest.setEmail("john@example.com");

        User user = new User();
        user.setName("John");
        user.setEmail("john@example.com");

        User savedUser = new User();
        savedUser.setId(123L);
        savedUser.setName("John");
        savedUser.setEmail("john@example.com");

        when(dtoMapperUtil.toEntity(userDtoRequest, User.class)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(savedUser);

        UserDtoResponse expectedDtoResponse = new UserDtoResponse();
        expectedDtoResponse.setId(123L);
        expectedDtoResponse.setName("John");
        expectedDtoResponse.setEmail("john@example.com");

        when(dtoMapperUtil.toDto(savedUser, UserDtoResponse.class)).thenReturn(expectedDtoResponse);

        // Act
        UserDtoResponse actualDtoResponse = userServiceUnderTest.createUser(userDtoRequest);

        // Assert
        assertThat(actualDtoResponse).isEqualTo(expectedDtoResponse);

        verify(dtoMapperUtil, times(1)).toEntity(userDtoRequest, User.class);
        verify(userRepository, times(1)).save(user);
        verify(dtoMapperUtil, times(1)).toDto(savedUser, UserDtoResponse.class);
    }

    @Test
    void updateUser_ExistingUser_ReturnsUpdatedUserDtoResponse() {
        // Arrange
        Long userId = 123L;
        UserDtoRequest userDtoRequest = new UserDtoRequest();
        userDtoRequest.setName("John Doe");
        userDtoRequest.setEmail("john.doe@example.com");

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setName("Old Name");
        existingUser.setEmail("old.email@example.com");

        UserDtoResponse expectedDtoResponse = new UserDtoResponse();
        expectedDtoResponse.setId(userId);
        expectedDtoResponse.setName("John Doe");
        expectedDtoResponse.setEmail("john.doe@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(dtoMapperUtil.toDto(existingUser, UserDtoResponse.class)).thenReturn(expectedDtoResponse);

        // Act
        UserDtoResponse actualDtoResponse = userServiceUnderTest.updateUser(userId, userDtoRequest);

        // Assert
        assertThat(actualDtoResponse).isEqualTo(expectedDtoResponse);

        verify(userRepository, times(1)).findById(userId);
        verify(dtoMapperUtil, times(1)).toDto(existingUser, UserDtoResponse.class);
    }

    @Test
    void updateUser_NonExistingUser_ThrowsEntityNotFoundException() {
        // Arrange
        Long userId = 123L;
        UserDtoRequest userDtoRequest = new UserDtoRequest();
        userDtoRequest.setName("John Doe");
        userDtoRequest.setEmail("john.doe@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> userServiceUnderTest.updateUser(userId, userDtoRequest));

        verify(userRepository, only()).findById(userId);
        verifyNoMoreInteractions(userRepository, dtoMapperUtil);
    }

    @Test
    void deleteUser_ExistingUser_DeletesUser() {
        // Arrange
        Long userId = 123L;
        User existingUser = new User();
        existingUser.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        // Act
        userServiceUnderTest.deleteUser(userId);

        // Assert
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).delete(existingUser);
    }

    @Test
    void deleteUser_NonExistingUser_ThrowsEntityNotFoundException() {
        // Arrange
        Long userId = 123L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> userServiceUnderTest.deleteUser(userId));

        verify(userRepository, times(1)).findById(userId);
        verifyNoMoreInteractions(userRepository);
    }
}
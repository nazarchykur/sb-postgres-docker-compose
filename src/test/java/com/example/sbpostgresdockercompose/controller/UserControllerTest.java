package com.example.sbpostgresdockercompose.controller;

import com.example.sbpostgresdockercompose.dto.UserDtoRequest;
import com.example.sbpostgresdockercompose.dto.UserDtoResponse;
import com.example.sbpostgresdockercompose.exception.EntityNotFoundException;
import com.example.sbpostgresdockercompose.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void getAllUsers_ReturnsListOfUsers() throws Exception {
        // Arrange
        UserDtoResponse user1 = new UserDtoResponse();
        user1.setId(1L);
        user1.setName("John Doe");
        user1.setEmail("john.doe@example.com");

        UserDtoResponse user2 = new UserDtoResponse();
        user2.setId(2L);
        user2.setName("Jane Smith");
        user2.setEmail("jane.smith@example.com");

        List<UserDtoResponse> userList = Arrays.asList(user1, user2);

        when(userService.getAllUsers()).thenReturn(userList);

        // Act & Assert
        mockMvc.perform(get("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(userList.size()))
                .andExpect(jsonPath("$[0].id").value(user1.getId()))
                .andExpect(jsonPath("$[0].name").value(user1.getName()))
                .andExpect(jsonPath("$[0].email").value(user1.getEmail()))
                .andExpect(jsonPath("$[1].id").value(user2.getId()))
                .andExpect(jsonPath("$[1].name").value(user2.getName()))
                .andExpect(jsonPath("$[1].email").value(user2.getEmail()));
    }

    @Test
    void getUserById_ExistingUser_ReturnsUserDtoResponse() throws Exception {
        // Arrange
        Long userId = 1L;
        UserDtoResponse expectedDtoResponse = new UserDtoResponse();
        expectedDtoResponse.setId(userId);
        expectedDtoResponse.setName("John Doe");
        expectedDtoResponse.setEmail("john.doe@example.com");

        when(userService.getUserById(userId)).thenReturn(expectedDtoResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedDtoResponse.getId()))
                .andExpect(jsonPath("$.name").value(expectedDtoResponse.getName()))
                .andExpect(jsonPath("$.email").value(expectedDtoResponse.getEmail()));
    }

    @Test
    void getUserById_NonExistingUser_ReturnsNotFound() throws Exception {
        // Arrange
        Long userId = 999L;

        when(userService.getUserById(userId)).thenThrow(new EntityNotFoundException("User not found with id=" + userId));

        // Act & Assert
        mockMvc.perform(get("/api/v1/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("404 NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("User not found with id=999"));
    }

    @Test
    void createUser_ReturnsCreatedUserDtoResponse() throws Exception {
        // Arrange
        UserDtoRequest userDtoRequest = new UserDtoRequest();
        userDtoRequest.setName("John Doe");
        userDtoRequest.setEmail("john.doe@example.com");

        UserDtoResponse expectedDtoResponse = new UserDtoResponse();
        expectedDtoResponse.setId(1L);
        expectedDtoResponse.setName("John Doe");
        expectedDtoResponse.setEmail("john.doe@example.com");

        when(userService.createUser(any(UserDtoRequest.class))).thenReturn(expectedDtoResponse);

        // Act & Assert
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Doe\",\"email\":\"john.doe@example.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/users/1"))
                .andExpect(jsonPath("$.id").value(expectedDtoResponse.getId()))
                .andExpect(jsonPath("$.name").value(expectedDtoResponse.getName()))
                .andExpect(jsonPath("$.email").value(expectedDtoResponse.getEmail()));
    }

    @Test
    void updateUser_ExistingUser_ReturnsUpdatedUserDtoResponse() throws Exception {
        // Arrange
        Long userId = 1L;
        UserDtoRequest userDtoRequest = new UserDtoRequest();
        userDtoRequest.setName("John Doe");
        userDtoRequest.setEmail("john.doe@example.com");

        UserDtoResponse expectedDtoResponse = new UserDtoResponse();
        expectedDtoResponse.setId(userId);
        expectedDtoResponse.setName("John Doe");
        expectedDtoResponse.setEmail("john.doe@example.com");

        when(userService.updateUser(userId, userDtoRequest)).thenReturn(expectedDtoResponse);

        // Act & Assert
        mockMvc.perform(put("/api/v1/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Doe\",\"email\":\"john.doe@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedDtoResponse.getId()))
                .andExpect(jsonPath("$.name").value(expectedDtoResponse.getName()))
                .andExpect(jsonPath("$.email").value(expectedDtoResponse.getEmail()));
    }

    @Test
    void updateUser_NonExistingUser_ReturnsNotFound() throws Exception {
        // Arrange
        Long userId = 999L;
        UserDtoRequest userDtoRequest = new UserDtoRequest();
        userDtoRequest.setName("John Doe");
        userDtoRequest.setEmail("john.doe@example.com");

        when(userService.updateUser(userId, userDtoRequest)).thenThrow(new EntityNotFoundException("User not found with id=" + userId));

        // Act & Assert
        mockMvc.perform(put("/api/v1/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Doe\",\"email\":\"john.doe@example.com\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("404 NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("User not found with id=999"));
    }

    @Test
    void deleteUser_ExistingUser_ReturnsNoContent() throws Exception {
        // Arrange
        Long userId = 1L;

        // Act & Assert
        mockMvc.perform(delete("/api/v1/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUser_NonExistingUser_ReturnsNotFound() throws Exception {
        // Arrange
        Long userId = 999L;

        doThrow(new EntityNotFoundException("User not found with id=" + userId)).when(userService).deleteUser(userId);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("404 NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("User not found with id=999"));
    }

}
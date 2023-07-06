package com.example.sbpostgresdockercompose.controller;

import com.example.sbpostgresdockercompose.dto.UserDtoRequest;
import com.example.sbpostgresdockercompose.dto.UserDtoResponse;
import com.example.sbpostgresdockercompose.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@Tag(name = "Users", description = "Users management APIs")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get all users", description = "Retrieves a list of all users")
    @ApiResponse(
            responseCode = "200",
            description = "Found the users",
            content = {@Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = UserDtoResponse.class)))
            })
    @GetMapping
    public ResponseEntity<List<UserDtoResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Get user by ID", description = "Retrieves a user by their ID")
    @Parameter(name = "id", description = "User's ID", required = true)
    @ApiResponse(
            responseCode = "200",
            description = "User found",
            content = {@Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserDtoResponse.class))
            })
    @ApiResponse(responseCode = "404", description = "User not found")
    @GetMapping("/{id}")
    public ResponseEntity<UserDtoResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Operation(summary = "Create a user", description = "Creates a new user")
    @Parameter(name = "userDtoRequest", description = "User DTO Request", required = true,
            schema = @Schema(implementation = UserDtoRequest.class))
    @ApiResponse(responseCode = "201", description = "User created",
            content = {@Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserDtoResponse.class))})
    @PostMapping
    public ResponseEntity<UserDtoResponse> createUser(@RequestBody UserDtoRequest userDtoRequest) {
        UserDtoResponse savedUser = userService.createUser(userDtoRequest);
        URI uri = URI.create("/api/v1/users/" + savedUser.getId());
        return ResponseEntity.created(uri).body(savedUser);
    }

    @Operation(summary = "Update a user", description = "Updates an existing user")
    @Parameter(name = "id", description = "User's ID", required = true)
    @Parameter(name = "userDtoRequest", description = "User DTO Request", required = true,
            schema = @Schema(implementation = UserDtoRequest.class))
    @ApiResponse(responseCode = "200", description = "User updated",
            content = {@Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserDtoResponse.class))})
    @ApiResponse(responseCode = "404", description = "User not found")
    @PutMapping("/{id}")
    public ResponseEntity<UserDtoResponse> updateUser(@PathVariable Long id, @RequestBody UserDtoRequest userDtoRequest) {
        return ResponseEntity.ok(userService.updateUser(id, userDtoRequest));
    }

    @Operation(summary = "Delete a user", description = "Deletes a user based on the provided ID")
    @Parameter(description = "ID of the user to be deleted", required = true)
    @ApiResponse(responseCode = "204", description = "User successfully deleted")
    @ApiResponse(responseCode = "404", description = "User not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

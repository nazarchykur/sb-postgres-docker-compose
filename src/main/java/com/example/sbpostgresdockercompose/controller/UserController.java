package com.example.sbpostgresdockercompose.controller;

import com.example.sbpostgresdockercompose.dto.UserDtoRequest;
import com.example.sbpostgresdockercompose.dto.UserDtoResponse;
import com.example.sbpostgresdockercompose.service.UserService;
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

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDtoResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDtoResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<UserDtoResponse> createUser(@RequestBody UserDtoRequest userDtoRequest) {
        UserDtoResponse savedUser = userService.createUser(userDtoRequest);
        URI uri = URI.create("/api/v1/users/" + savedUser.id());
        return ResponseEntity.created(uri).body(savedUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDtoResponse> updateUser(@PathVariable Long id, @RequestBody UserDtoRequest userDtoRequest) {
        return ResponseEntity.ok(userService.updateUser(id, userDtoRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

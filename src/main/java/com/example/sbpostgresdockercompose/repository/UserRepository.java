package com.example.sbpostgresdockercompose.repository;

import com.example.sbpostgresdockercompose.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository <User, Long> {
}

package com.example.management.user.service;

import com.example.management.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    User saveUser(User user);
    void deleteUser(Long id);
    boolean emailExists(String email);
    long getUserCount();
    Optional<User> getUserByEmail(String email);
}
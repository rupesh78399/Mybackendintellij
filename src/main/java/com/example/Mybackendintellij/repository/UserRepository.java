package com.example.Mybackendintellij.repository;

import com.example.Mybackendintellij.model.UserEntity;
import com.example.Mybackendintellij.model.UserModel;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {

    private final List<UserEntity> users = new ArrayList<>();
    private Long idCounter = 1L;

    public UserEntity save(@MonotonicNonNull UserEntity user) {
        user.setId(idCounter++);
        users.add(user);
        return user;
    }

    public UserEntity findById(Long id) {
        return users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);
    }


    public UserEntity findByPhone(String phone) {
        return users.stream()
                .filter(u -> u.getPhone().equals(phone))
                .findFirst()
                .orElse(null);
    }

    public UserEntity login(String phone, String password) {
        return users.stream()
                .filter(u -> u.getPhone().equals(phone) && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }
}

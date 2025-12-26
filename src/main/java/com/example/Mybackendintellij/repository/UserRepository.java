package com.example.Mybackendintellij.repository;

import com.example.Mybackendintellij.model.UserModel;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {

    private final List<UserModel> users = new ArrayList<>();
    private Long idCounter = 1L;

    public UserModel save(UserModel user) {
        user.setId(idCounter++);
        users.add(user);
        return user;
    }

    public UserModel findByPhone(String phone) {
        return users.stream()
                .filter(u -> u.getPhone().equals(phone))
                .findFirst()
                .orElse(null);
    }

    public UserModel login(String phone, String password) {
        return users.stream()
                .filter(u -> u.getPhone().equals(phone) && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }
}

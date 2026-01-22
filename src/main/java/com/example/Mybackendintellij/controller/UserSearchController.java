package com.example.Mybackendintellij.controller;

import com.example.Mybackendintellij.dto.UserDto;
import com.example.Mybackendintellij.repository.userRepoforSetImage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserSearchController {

    private final userRepoforSetImage userRepo;

    public UserSearchController(userRepoforSetImage userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping("/search")
    public List<UserDto> search(@RequestParam String username) {

        return userRepo
                .findByUsernameContainingIgnoreCase(username)
                .stream()
                .map(user -> {
                    UserDto dto = new UserDto();
                    dto.setId(user.getId());
                    dto.setName(user.getName());
                    dto.setUsername(user.getUsername()); // ✅ ADD THIS
                    dto.setImagePath(user.getImagePath());
                    return dto;
                })
                .toList();
    }
}

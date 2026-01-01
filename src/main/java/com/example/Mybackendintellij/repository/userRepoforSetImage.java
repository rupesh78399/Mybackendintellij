package com.example.Mybackendintellij.repository;

import com.example.Mybackendintellij.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface userRepoforSetImage extends JpaRepository<UserEntity, Long> {
    UserEntity findByPhone(String phone);
}

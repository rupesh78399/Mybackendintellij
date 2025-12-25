package com.example.Mybackendintellij.repository;

import com.example.Mybackendintellij.model.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepoMsg  extends JpaRepository<MyUser, Long> {

    @Query("SELECT u.fcmToken FROM MyUser u WHERE u.id = :userId")
    String findFcmTokenByUserId(@Param("userId") Long userId);

}
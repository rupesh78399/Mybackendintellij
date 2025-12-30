package com.example.Mybackendintellij.controller;

import com.example.Mybackendintellij.model.MyUser;
import com.example.Mybackendintellij.model.UserModel;
import com.example.Mybackendintellij.repository.UserRepoMsg;
import com.example.Mybackendintellij.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private final UserRepository userRepo;
    private final UserRepoMsg userRepoMsg;

    public AuthController(UserRepository userRepo, UserRepoMsg userRepoMsg) {
        this.userRepo = userRepo;
        this.userRepoMsg = userRepoMsg;
    }

    @PostMapping(value = "/signup", consumes = "multipart/form-data")
    public ResponseEntity<?> signup( @RequestParam("name") String name,
                                     @RequestParam("phone") String phone,
                                     @RequestParam("password") String password,
                                     @RequestParam("image") MultipartFile image) throws Exception {
        if (userRepo.findByPhone(phone) != null) {
            return ResponseEntity.badRequest().body("MyUser already exists");
        }

        // Save image on server
        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        Path path = Paths.get("/tmp/uploads");
        if(!Files.exists(path)){
            Files.createDirectories(path);
        }
        Path filePath = path.resolve(fileName);
        Files.copy(image.getInputStream() , filePath , StandardCopyOption.REPLACE_EXISTING);

        String imageUrl = "/uploads/" + fileName;

        UserModel user = new UserModel();
        user.setName(name);
        user.setPhone(phone);
        user.setPassword(password);
        user.setImagePath(imageUrl);
        UserModel savedUser = userRepo.save(user);

        MyUser Mu = new MyUser();
        Mu.setId(savedUser.getId());
        Mu.setPhone(savedUser.getPhone());     // ✅ SAVE PHONE
        Mu.setFcmToken(null);
        userRepoMsg.save(Mu);

        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserModel user) {
        UserModel found = userRepo.login(user.getPhone(), user.getPassword());
        if (found == null) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
        return ResponseEntity.ok(found);
    }
}

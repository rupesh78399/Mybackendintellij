package com.example.Mybackendintellij;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String phone;
    private String password;
    private String imagePath;

    public UserModel() {}

    public UserModel(Integer id, String name, String phone, String password, String imagePath) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.imagePath = imagePath;
    }

    // Getters & Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
}

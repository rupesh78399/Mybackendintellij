package com.example.Mybackendintellij;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dghtebw8l");
        config.put("api_key", "838732768524838");
        config.put("api_secret", "Ms5zdxNBcGmWGj9SIDMRge0yIlQ");
        return new Cloudinary(config);
    }
}

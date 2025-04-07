package com.cordingrecipe.groupnewsfeed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class GroupNewsfeedApplication {

    public static void main(String[] args) {
        SpringApplication.run(GroupNewsfeedApplication.class, args);
    }

}

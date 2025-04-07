package com.cordingrecipe.groupnewsfeed.user.service;

import com.cordingrecipe.groupnewsfeed.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
}

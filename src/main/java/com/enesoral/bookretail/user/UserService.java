package com.enesoral.bookretail.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public boolean isUserExist(String id) {
        return userRepository.existsById(id);
    }
}

package com.enesoral.bookretail.user;

import com.enesoral.bookretail.common.exception.EmailAlreadyTakenException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public boolean isUserExist(String id) {
        return userRepository.existsById(id);
    }

    public User save(UserCommand userCommand) {
        if (userRepository.existsByEmail(userCommand.getEmail())) {
            throw new EmailAlreadyTakenException(userCommand.getEmail());
        }

        userCommand.setPassword(passwordEncoder.encode(userCommand.getPassword()));
        return userRepository.save(toDocument(userCommand));
    }

    private User toDocument(UserCommand userCommand) {
        return userMapper.toDocument(userCommand);
    }
}

package com.enesoral.bookretail.user;

import com.enesoral.bookretail.common.exception.EmailAlreadyTakenException;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)

class UserServiceTest {

    public static final String ID = ObjectId.get().toString();
    public static final String FULL_NAME = "Enes Oral";
    public static final String EMAIL = "enesoral@gmail.com";

    public static User userResponse;
    public static User userRequest;
    public static UserCommand userCommand;

    @Spy
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        userResponse = User.builder()
                .id(ID)
                .fullName(FULL_NAME)
                .email(EMAIL)
                .build();

        userRequest = User.builder()
                .fullName(FULL_NAME)
                .email(EMAIL)
                .build();

        userCommand = userMapper.toCommand(userResponse);
    }

    @Test
    void givenValidUserCommand_whenPerformSaving_thenReturnSuccessResponse() {
        lenient().when(userRepository.save(userRequest))
                .thenReturn(userResponse);

        final User savedUser = userService.save(userCommand);

        verify(userRepository, times(1)).save(userRequest);
        assertThat(savedUser)
                .isNotNull()
                .matches(user -> user.getId().equals(userResponse.getId()))
                .matches(user -> user.getFullName().equals(userResponse.getFullName()))
                .matches(user -> user.getEmail().equals(userResponse.getEmail()));
    }

    @Test
    void givenValidUserCommand_whenEmailAlreadyExists_thenException() {
        lenient().when(userRepository.save(userRequest))
                .thenReturn(userResponse);

        userService.save(userCommand);

        lenient().when(userRepository.existsByEmail(userRequest.getEmail()))
                .thenReturn(true);

        verify(userRepository, times(1)).existsByEmail(userRequest.getEmail());
        assertThrows(EmailAlreadyTakenException.class, () -> userService.save(userCommand));
    }

    @Test
    void givenUserId_whenUserExist_thenReturnTrue() {
        lenient().when(userRepository.save(userRequest))
                .thenReturn(userResponse);

        final User savedUser = userService.save(userCommand);

        lenient().when(userRepository.existsById(savedUser.getId()))
                .thenReturn(true);

        final boolean userExist = userService.isUserExist(savedUser.getId());

        verify(userRepository, times(1)).existsById(savedUser.getId());
        assertTrue(userExist);
    }
}
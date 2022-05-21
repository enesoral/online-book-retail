package com.enesoral.bookretail.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static com.enesoral.bookretail.user.UserTestHelper.EMAIL;
import static com.enesoral.bookretail.user.UserTestHelper.FULL_NAME;
import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureDataMongo
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles(value = "test")
class UserIntegrationTest {

    @Autowired
    private UserTestHelper testHelper;

    @Autowired
    private UserRepository userRepository;

    @Test
    void givenValidUserCommand_whenPerformSaving_thenReturnSuccessResponse() throws Exception {
        testHelper.createUser(FULL_NAME, EMAIL);

        final Optional<User> userByEmail = userRepository.findByEmail(EMAIL);
        assertThat(userByEmail)
                .isPresent()
                .map(User::getId)
                .isNotNull();

        assertThat(userByEmail.get())
                .matches(user -> user.getFullName().equals(FULL_NAME))
                .matches(user -> user.getEmail().equals(EMAIL));
    }
}

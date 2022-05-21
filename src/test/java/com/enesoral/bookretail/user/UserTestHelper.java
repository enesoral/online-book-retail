package com.enesoral.bookretail.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Component
public class UserTestHelper {

    public static final String ID = ObjectId.get().toString();
    public static final String FULL_NAME = "Enes Oral";
    public static final String FULL_NAME_2 = "Ali Şen";
    public static final String EMAIL = "abc@gmail.com";
    public static final String EMAIL_2 = "xyz@gmail.com";

    private final WebApplicationContext applicationContext;

    private final ObjectMapper objectMapper;

    private final MockMvc mvc;

    public UserTestHelper(WebApplicationContext webApplicationContext, ObjectMapper objectMapper) {
        this.applicationContext = webApplicationContext;
        this.objectMapper = objectMapper;
        this.mvc = MockMvcBuilders.webAppContextSetup(this.applicationContext).build();
    }

    public User createUser(String fullName, String email) throws Exception {
        final UserCommand newUser = UserCommand.builder()
                .fullName(fullName)
                .email(email)
                .build();

        return objectMapper.readValue(
                mvc.perform(
                        MockMvcRequestBuilders.post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(newUser))
                        )
                        .andExpect(status().isCreated())
                        .andReturn().getResponse().getContentAsString(), User.class);
    }
}

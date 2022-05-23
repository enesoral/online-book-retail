package com.enesoral.bookretail.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
@EqualsAndHashCode
@Builder
public class User {

    @Id
    private String id;

    @JsonIgnore
    private String password;

    private String fullName;

    @Indexed(unique = true)
    private String email;
}

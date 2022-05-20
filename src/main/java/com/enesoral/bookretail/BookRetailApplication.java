package com.enesoral.bookretail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class BookRetailApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookRetailApplication.class, args);
	}

}

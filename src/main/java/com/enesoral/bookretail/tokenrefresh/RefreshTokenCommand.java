package com.enesoral.bookretail.tokenrefresh;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class RefreshTokenCommand {

	@NotBlank
	private String refreshToken;

	@Email
	@NotBlank
	private String email;
}

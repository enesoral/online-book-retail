package com.enesoral.bookretail.refreshtoken;

import com.enesoral.bookretail.common.exception.RefreshTokenNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

	private final RefreshTokenRepository refreshTokenRepository;

	public RefreshToken generateRefreshToken() {
		final RefreshToken refreshToken = RefreshToken.builder()
				.token(UUID.randomUUID().toString())
				.creationDate(LocalDateTime.now())
				.build();
		return refreshTokenRepository.save(refreshToken);
	}

	public void deleteRefreshToken(String token) {
		refreshTokenRepository.deleteByToken(token);
	}

	public void validateRefreshToken(String token) {
		refreshTokenRepository.findByToken(token)
				.orElseThrow(() -> new RefreshTokenNotFoundException(token));
	}
}

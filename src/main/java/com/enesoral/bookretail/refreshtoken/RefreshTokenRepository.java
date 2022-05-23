package com.enesoral.bookretail.refreshtoken;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {
	Optional<RefreshToken> findByToken(String token);

	void deleteByToken(String token);
}

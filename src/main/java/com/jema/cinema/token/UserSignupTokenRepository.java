package com.jema.cinema.token;

import com.jema.cinema.user.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserSignupTokenRepository extends JpaRepository<UserSignupVerificationToken, Long> {
    Optional<UserSignupVerificationToken> findByToken(String token);
    Optional<UserSignupVerificationToken> findByUserModel(UserModel userModel);
}

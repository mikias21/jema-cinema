package com.jema.cinema.token;

import com.jema.cinema.user.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PasswordTokenRepository extends JpaRepository<PasswordRecoveryToken, Long> {
    Optional<PasswordRecoveryToken> findByToken(String token);
    Optional<PasswordRecoveryToken> findByUserModel(UserModel userModel);
}

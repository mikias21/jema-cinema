package com.jema.cinema.token;

import com.jema.cinema.user.UserModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserTokenService {
    private UserSignupTokenRepository userSignupTokenRepository;
    private UserSignupVerificationToken userSignupVerificationToken;
    private PasswordRecoveryToken passwordRecoveryToken;
    private PasswordTokenRepository passwordTokenRepository;

    public void saveSignupConfirmationToken(UserModel userModel, String token){
        userSignupVerificationToken.setToken(token);
        userSignupVerificationToken.setUserModel(userModel);
        userSignupVerificationToken.setExpiryDate(userSignupVerificationToken.calculateExpiryDate(120));
        userSignupTokenRepository.save(userSignupVerificationToken);
    }

    public void savePasswordRecoveryToken(UserModel userModel, String token){
        passwordRecoveryToken.setToken(token);
        passwordRecoveryToken.setUserModel(userModel);
        passwordRecoveryToken.setExpiryDate(userSignupVerificationToken.calculateExpiryDate(120));
        passwordTokenRepository.save(passwordRecoveryToken);
    }

    public Optional<UserSignupVerificationToken> getUserSignupVerificationToken(String token){
        Optional<UserSignupVerificationToken> userSignupVerificationToken = userSignupTokenRepository.findByToken(token);
        return userSignupVerificationToken;
    }

    public Optional<PasswordRecoveryToken> getPasswordToken(String token){
        Optional<PasswordRecoveryToken> passwordRecoveryToken = passwordTokenRepository.findByToken(token);
        return passwordRecoveryToken;
    }

}

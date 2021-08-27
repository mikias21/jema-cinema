package com.jema.cinema.user;

import com.jema.cinema.token.UserTokenService;
import com.jema.cinema.utils.JwtToken;
import com.jema.cinema.utils.MainEmailSender;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import javax.mail.MessagingException;
import java.util.UUID;

@Component
public class UserAccountListener implements ApplicationListener<OnSignupCompleteEvent> {

    @Autowired
    private UserTokenService userTokenService;
    @Autowired
    private MainEmailSender mainEmailSender;

    @SneakyThrows
    @Override
    public void onApplicationEvent(OnSignupCompleteEvent event) {
        this.confirmSignup(event);
    }

    private void confirmSignup(OnSignupCompleteEvent event) throws MessagingException {
        UserModel userModel = event.getUserModel();
        String token = UUID.randomUUID().toString();
        // persist the token to a table
        userTokenService.saveSignupConfirmationToken(userModel, token);
        // make url
        String confirmationURl = event.getUrl() + "/activate/"+token;
        mainEmailSender.sendActivationEmail(userModel.getEmail(), confirmationURl);
    }
}

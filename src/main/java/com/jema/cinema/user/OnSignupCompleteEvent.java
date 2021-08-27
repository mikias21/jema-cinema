package com.jema.cinema.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
import java.util.Locale;

@Setter
@Getter
public class OnSignupCompleteEvent extends ApplicationEvent {
    private String url;
    private Locale locale;
    private UserModel userModel;

    public OnSignupCompleteEvent(UserModel userModel, Locale locale, String url) {
        super(userModel);
        this.userModel = userModel;
        this.locale = locale;
        this.url = url;
    }
}

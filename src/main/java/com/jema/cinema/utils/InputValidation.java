package com.jema.cinema.utils;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@NoArgsConstructor
@Component
public class InputValidation {

    // regex and pattern matchers to validate email, password and name
    private final String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
            "[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
            "A-Z]{2,7}$";
    private final String nameRegex = "(?i)(^[a-z])((?![ .,'-]$)[a-z .,'-]){0,24}$";
    private final String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
    private Pattern emailPattern = Pattern.compile(emailRegex);
    private Pattern namePattern = Pattern.compile(nameRegex);
    private Pattern passwordPattern = Pattern.compile(passwordRegex);

    //    ^ # start-of-string
    //    (?=.*[0-9]) # a digit must occur at least once
    //    (?=.*[a-z]) # a lower case letter must occur at least once
    //    (?=.*[A-Z]) # an upper case letter must occur at least once
    //    (?=.*[@#$%^&+=])  # a special character must occur at least once
    //    (?=\S+$)    # no whitespace allowed in the entire string
    //    .{8,}       # anything, at least eight places though
    //    $ # end-of-string

    // function to validate name inputs
    public boolean validateName(String name){
        if(name.trim().length() == 0) return false;
        if(name.trim().length() > 20) return false;
        return namePattern.matcher(name).matches();
    }

    // function to validate email inputs
    public boolean validateEmail(String email){
        return emailPattern.matcher(email).matches();
    }

    // function to validate password input
    public boolean validatePassword(String password){
        return passwordPattern.matcher(password).matches();
    }
}

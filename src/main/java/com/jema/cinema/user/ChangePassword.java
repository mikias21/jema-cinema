package com.jema.cinema.user;

import lombok.*;
import org.springframework.stereotype.Component;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Component
@ToString
public class ChangePassword {
    private String userEmail;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}

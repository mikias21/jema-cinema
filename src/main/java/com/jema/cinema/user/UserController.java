package com.jema.cinema.user;

import com.jema.cinema.exception.MainRequestException;
import com.jema.cinema.token.PasswordRecoveryToken;
import com.jema.cinema.token.UserTokenService;
import com.jema.cinema.token.UserSignupVerificationToken;
import com.jema.cinema.utils.MainEmailSender;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping(path = "/jema")
@AllArgsConstructor
//@CrossOrigin(origins = "http://127.0.0.1:3000"  )
public class UserController{

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private UserTokenService userTokenService;
    @Autowired
    private MainEmailSender mainEmailSender;
    @Autowired
    private final UserService userService;
    private UserResponse userResponse;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserProfilePictureService userProfilePictureService;

    // configuration for cors
    @Bean
    public WebMvcConfigurer configure(){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/*").allowedOrigins("*")
                        .allowedHeaders("*")
                        .allowedMethods("*")
                        .allowCredentials(true);
            }
        };
    }


    @PostMapping("/user/signup")
    public ResponseEntity<?> createNewUser(@RequestBody @Validated UserModel userModel, HttpServletRequest request, Errors errors){
        userModel.setDeviceIp(request.getRemoteAddr());
        try{
            if(userService.signUpUser(userModel)){
                String url = request.getContextPath();
                applicationEventPublisher.publishEvent(new OnSignupCompleteEvent(userModel, request.getLocale(), url));
                // set session data
                UserResponse userResponse = new UserResponse(
                        userModel.getFirstName(),
                        userModel.getLastName(),
                        userModel.getEmail(),
                        "Account created Successfully, Please activate account with in 2 hours",
                        200
                );
                return ResponseEntity.ok().body(userResponse);
            }
        }catch(MainRequestException e){
            UserResponse userResponse = new UserResponse(e.getMessage(), 403);
            return ResponseEntity.status(403).body(userResponse);
        }
        UserResponse userResponse = new UserResponse("Invalid request, please try again", 401);
        return ResponseEntity.status(403).body(userResponse);
    }


    @GetMapping("/user/activate")
    public ResponseEntity<?> activateAccount(@RequestParam("token") String token){
        Optional<UserSignupVerificationToken> userSignupVerificationToken =
                userTokenService.getUserSignupVerificationToken(token);

        if(userSignupVerificationToken.isEmpty()){
            userResponse.setResponse("Invalid Activation Code");
            return ResponseEntity.ok(userResponse);
        }else{
            // get user
            UserModel userModel = userSignupVerificationToken.get().getUserModel();
            Calendar calendar = Calendar.getInstance();
            if((userSignupVerificationToken.get().getExpiryDate().getTime() - calendar.getTime().getTime()) <= 0){
                userResponse.setResponse("Activation code is expired");
                return ResponseEntity.ok(userResponse);
            }else{
                userModel.setAccountIsActive(true);
                userService.saveUser(userModel);
                userResponse.setResponse("Account is activated");
                return ResponseEntity.ok(userResponse);
            }
        }
    }

    // user login function
    @PostMapping("/user/login")
    public ResponseEntity<?> loginUser(@RequestBody @Validated UserLoginData userLoginData,
                                       HttpServletRequest httpServletRequest){

        userLoginData.setLoginDeviceIp(httpServletRequest.getRemoteAddr());

        try{
            if(userService.loginUser(userLoginData)){
                UserResponse userResponse = new UserResponse(
                        userLoginData.getUserModel().getFirstName(),
                        userLoginData.getUserModel().getLastName(),
                        userLoginData.getEmail(),
                        userLoginData.getUserModel().getProfilePicture(),
                        200
                );
                userService.manageUserSession(httpServletRequest, userResponse);
                return ResponseEntity.ok().body(userResponse);
            }
        }catch(Exception e){
            UserResponse userResponse = new UserResponse(e.getMessage(), 403);
            return ResponseEntity.status(403).body(userResponse);
        }
        return ResponseEntity.status(403).body("Not allowed");
    }

    // account recovery
    @GetMapping("/user/recover")
    public ResponseEntity<?> recoverAccount(@RequestParam String email, HttpServletRequest httpServletRequest){
        // check if user is found
        try{
            UserModel userModel = userService.findUserByEmail(email).get();
            String url = httpServletRequest.getContextPath();
            if(userModel != null){
                // generate token
                String token = UUID.randomUUID().toString();
                userTokenService.savePasswordRecoveryToken(userModel, token);
                String recoveryLink = url + "/changepassword/" + token;
                mainEmailSender.sendAccountRecoveryEmail(userModel.getEmail(), recoveryLink);
                userResponse.setResponse("Recovery Link has been send to your email");
                userResponse.setStatus(200);
                userResponse.setEmail(email);
                return ResponseEntity.ok(userResponse);
            }
        }catch (Exception e){
            userResponse.setResponse("Email is invalid");
            userResponse.setStatus(401);
            return ResponseEntity.ok(userResponse);
        }
        return null;



    }

    // Change password
    @PostMapping("/user/recoverpassword")
    public ResponseEntity<?> recoverPassword(@RequestBody @Validated PasswordRecover passwordRecover, HttpServletRequest httpServletRequest){
        String token = passwordRecover.getToken();
        if(token != null){
            // get user model
            Optional<PasswordRecoveryToken> passwordRecoveryToken = userTokenService.getPasswordToken(token);
            if(passwordRecoveryToken.isEmpty()){
                userResponse.setStatus(401);
                userResponse.setResponse("Invalid request, please try again");
                return ResponseEntity.ok(userResponse);
            }else{
                UserModel userModel = passwordRecoveryToken.get().getUserModel();
                Calendar calendar = Calendar.getInstance();
                if((passwordRecoveryToken.get().getExpiryDate().getTime() - calendar.getTime().getTime()) <= 0){
                    userResponse.setStatus(401);
                    userResponse.setResponse("Link is expired, please try again");
                    return ResponseEntity.ok(userResponse);
                }else{
                    try{
                        userService.updatePassword(userModel, passwordRecover.getPassword());
                        userResponse.setStatus(200);
                        userResponse.setResponse("Password Changed Successfully");
                        return ResponseEntity.ok(userResponse);
                    }catch(Exception e){
                        userResponse.setStatus(401);
                        userResponse.setResponse(e.getMessage());
                        return ResponseEntity.ok(userResponse);
                    }
                }
            }
        }else{
            userResponse.setStatus(401);
            userResponse.setResponse("Invalid request, please try again");
            return ResponseEntity.ok(userResponse);
        }
    }

    @PostMapping("/user/changepassword")
    public ResponseEntity<?> changePassword(HttpServletRequest httpServletRequest,
                                            @RequestBody @Validated ChangePassword changePassword){
        if(changePassword.getUserEmail().length() != 0){
            // get user by email
            try{
                UserModel userModel = userService.findUserByEmail(changePassword.getUserEmail()).get();
                if(userModel != null){
                  if(bCryptPasswordEncoder.matches(changePassword.getOldPassword(), userModel.getPassword())){
                    if(changePassword.getNewPassword().equals(changePassword.getConfirmPassword())){
                        try{
                            userService.updatePassword(userModel, changePassword.getNewPassword());
                            return ResponseEntity.ok().body(1);
                        }catch (Exception e){
                            return ResponseEntity.badRequest().body(e.getMessage());
                        }
                    }
                    return ResponseEntity.badRequest().body("Password don't match");
                  }
                  return ResponseEntity.badRequest().body("Wrong old password");
                }
                return ResponseEntity.badRequest().body("User Not Found");
            }catch(Exception e){
                return ResponseEntity.badRequest().body("User not found");
            }
        }else{
            return ResponseEntity.badRequest().body("User Not Found");
        }
    }

    @PostMapping("/user/uploadpicture")
    public ResponseEntity<?> uploadProfilePicture(@RequestParam("file") MultipartFile file,
                                                  @RequestParam("email") String email){
        userProfilePictureService.saveProfilePicture(file);
        // update user model
        UserModel userModel = userService.findUserByEmail(email).get();
        if(userModel != null){
            userModel.setProfilePicture(file.getOriginalFilename());
            userService.saveUser(userModel);
            return ResponseEntity.ok().body(file.getOriginalFilename());
        }
        return ResponseEntity.ok().body(1);
    }

    @GetMapping("/user/getpicture")
    public ResponseEntity<?> getProfilePicture(@RequestParam("email") String email){
        UserModel userModel = userService.findUserByEmail(email).get();
        if(userModel != null){
            return ResponseEntity.ok().body(userModel.getProfilePicture());
        }
        return ResponseEntity.ok().body(1);
    }

    @PostMapping("/user/logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest httpServletRequest){
        httpServletRequest.getSession().invalidate();
        return ResponseEntity.ok().build();
    }


}

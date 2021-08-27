package com.jema.cinema.user;

import com.jema.cinema.exception.MainRequestException;
import com.jema.cinema.utils.InputValidation;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Component
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserLoginRepository userLoginRepository;
    private final InputValidation inputValidation;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public boolean signUpUser(UserModel userModel){
        // Do validation here
        // validate usernames
        if(!inputValidation.validateName(userModel.getFirstName()) ||
            !inputValidation.validateName(userModel.getLastName())){
            throw new MainRequestException("Name Fields can not be more than 20 and letters allowed only");
        }else if(!inputValidation.validateEmail(userModel.getEmail())){
            throw new MainRequestException("Please use a valid email");
        }else if(!inputValidation.validatePassword(userModel.getPassword())){
            throw new MainRequestException("You password should be at least 8 characters," +
                    "and must have small letter, capital letter, " +
                    "digit and special character");
        }else{
            // Check if user is already signed up
            Optional<?> user = userRepository.findByEmail(userModel.getEmail());
            if(user.isPresent()){
                throw new MainRequestException("User is already registered with this email");
            }else{
                // hash the password
                String hashedPassword = bCryptPasswordEncoder.encode(userModel.getPassword());
                userModel.setPassword(hashedPassword);
                userRepository.save(userModel);
                return true;
            }
        }
    }

    public boolean loginUser(UserLoginData userLoginData){
        if(!inputValidation.validateEmail(userLoginData.getEmail())){
            throw new MainRequestException("Please use a valid email to login");
        }else{
            // get the user
            Optional<UserModel> userModel = userRepository.findByEmail(userLoginData.getEmail());
            if(userModel.isPresent()){
                // check if the account is activated
                if(userModel.get().getAccountIsActive()){
                    if(userModel.get().getAccountIsLocked()){
                        throw new MainRequestException("Your account is locked for some reason");
                    }else{
                        // validate password
                        if(bCryptPasswordEncoder.matches(userLoginData.getPassword(), userModel.get().getPassword())){

                            // update values to the userLogin Model class
                            userLoginData.setUserModel(userModel.get());
                            userLoginData.setPassword(userModel.get().getPassword());
                            userLoginData.setUserId(userModel.get().getUserId());
                            // save login data to table
                            userLoginRepository.save(userLoginData);
                            return true;
                        }else{
                            throw new MainRequestException("Email and password combination is wrong");
                        }
                    }
                }else{
                    throw new MainRequestException("Please activate your account first");
                }
            }else{
                throw new MainRequestException("User with this email not found");
            }
        }
    }

    public void saveUser(UserModel userModel){
        userRepository.save(userModel);
    }

    public Optional<UserModel> findUserByEmail(String email){
        if(inputValidation.validateEmail(email))
            return userRepository.findByEmail(email);
        else
            throw new MainRequestException("Email is invalid");
    }

    public void updatePassword(UserModel userModel, String password){
        if(!inputValidation.validatePassword(password)){
            throw new MainRequestException("You password should be at least 8 characters," +
                    "and must have small letter, capital letter, " +
                    "digit and special character");
        }else{
            String hashed = bCryptPasswordEncoder.encode(password);
            userModel.setPassword(hashed);
            userRepository.save(userModel);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return null;
    }

    public void manageUserSession(HttpServletRequest httpServletRequest, UserResponse userResponse){
        List<UserResponse> userSession = (List<UserResponse>) httpServletRequest
                .getSession().getAttribute("USER_LOGIN_SESSIONS");
        if(userSession == null){
            userSession = new ArrayList<>();
            httpServletRequest.getSession().setAttribute("USER_LOGIN_SESSIONS", userSession);
        }
        userSession.add(userResponse);
        httpServletRequest.getSession().setAttribute("USER_LOGIN_SESSIONS", userSession);
    }

}

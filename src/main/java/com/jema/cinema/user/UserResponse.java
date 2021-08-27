package com.jema.cinema.user;

import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class UserResponse implements Serializable {
    private String firstName;
    private String lastName;
    private String email;
    private String profilePicture;
    private String response;
    private int status;

    public UserResponse(){}

    public UserResponse(String firstName, String lastName, String email, String profilePicture, int status){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.profilePicture = profilePicture;
        this.response = "";
        this.status = status;
    }

    public UserResponse(String response, int status){
        this.response = response;
        this.status = status;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

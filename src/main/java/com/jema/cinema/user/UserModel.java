package com.jema.cinema.user;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@EqualsAndHashCode
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class UserModel {

    @Id
    @Column(insertable = false, updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String userId = UUID.randomUUID().toString();
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false, unique = true)
    private String email;
    @Column()
    private String profilePicture;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String signUpUserAgent;
    @Column(nullable = false)
    private String deviceIp;
    @Column(nullable = false)
    private Double signUpLongitude;
    @Column(nullable = false)
    private Double signUpLatitude;
    @Column(nullable = false)
    private LocalDate signUpDate = LocalDate.now();
    @Column(nullable = false)
    private String accountRole = "USER";
    @Column(nullable = false)
    private Boolean accountIsActive = false;
    @Column(nullable = false)
    private Boolean accountIsLocked = false;
}

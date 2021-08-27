package com.jema.cinema.favoritemovie;

import com.jema.cinema.user.UserModel;
import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Component
public class FavoriteMovie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String movieName;
    @Column(nullable = false)
    private String movieId;
    @Column(nullable = false)
    private String moviePictureUrl;
    @Column(nullable = false)
    private String movieVideoUrl;
    private String movieMeta;
    @Column(nullable = false)
    private String likedDate = LocalDateTime.now().toString();
    @ManyToOne(targetEntity = UserModel.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_fav_id")
    private UserModel userModel;

}

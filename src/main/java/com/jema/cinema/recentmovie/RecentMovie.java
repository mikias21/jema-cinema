package com.jema.cinema.recentmovie;

import com.jema.cinema.user.UserModel;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Entity
public class RecentMovie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, unique = true)
    private String movieId;
    @Column(nullable = false)
    private String movieName;
    @Column(nullable = false)
    private String movieImageUrl;
    @Column(nullable = false)
    private String movieVideoUrl;
    @Column(nullable = false)
    private String meta;
    @Column(nullable = false)
    private String addedDate = LocalDateTime.now().toString();
    @ManyToOne(targetEntity = UserModel.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_recent_id")
    private UserModel userModel;
}

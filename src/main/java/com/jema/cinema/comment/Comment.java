package com.jema.cinema.comment;

import com.jema.cinema.user.UserModel;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String movieId;
    @Column(nullable = false)
    private String movieName;
    @Column(nullable = false)
    private String movieImage;
    @Column(nullable = false)
    private String movieVideo;
    private String movieMeta;
    @Column(nullable = false)
    private int rate;
    private String comment;
    private String commentedDateTime = LocalDateTime.now().toString();
    @ManyToOne(targetEntity = UserModel.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_comment_id")
    private UserModel userModel;
}

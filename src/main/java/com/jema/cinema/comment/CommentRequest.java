package com.jema.cinema.comment;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class CommentRequest {
    private String movieId;
    private String movieName;
    private String movieImage;
    private String movieVideo;
    private String movieMeta;
    private String userEmail;
    private int rate;
    private String comment;
}


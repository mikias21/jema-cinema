package com.jema.cinema.favoritemovie;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class FavoriteMovieAddRequest {
    private String email;
    private String movieId;
    private String movieName;
    private String moviePictureUrl;
    private String movieVideoUrl;
    private String movieMeta;
}

package com.jema.cinema.recentmovie;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RecentMovieRequest {
    private String movieId;
    private String movieName;
    private String movieImageUrl;
    private String movieVideoUrl;
    private String meta;
    private String email;
}

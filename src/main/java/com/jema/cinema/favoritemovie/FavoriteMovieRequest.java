package com.jema.cinema.favoritemovie;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
public class FavoriteMovieRequest {
    private String email = "";
    private String movieId = "";
}

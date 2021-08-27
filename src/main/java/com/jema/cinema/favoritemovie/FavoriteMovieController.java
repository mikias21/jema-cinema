package com.jema.cinema.favoritemovie;

import com.jema.cinema.user.UserModel;
import com.jema.cinema.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/jema/user/movie")
@AllArgsConstructor
public class FavoriteMovieController {

    @Autowired
    private UserService userService;
    private final FavoriteMovieService favoriteMovieService;

    // cors config
    @Bean
    public WebMvcConfigurer corsConfigure(){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/*").allowedOrigins("*")
                        .allowedHeaders("*")
                        .allowedMethods("*")
                        .allowCredentials(true);
            }
        };
    }

    @GetMapping("/favorite")
    public ResponseEntity<?> getFavoriteMovie(@RequestParam String email,
                                              HttpServletRequest httpServletRequest){
        // get user with given email
        Optional<UserModel> userModel = userService.findUserByEmail(email);
        // get favorite movie with user data
        List<FavoriteMovie> favoriteMovie = null;
        if(userModel.isPresent()){
             favoriteMovie = favoriteMovieService.getUserFavoriteMovieByUser(userModel.get());
        }
        favoriteMovie.forEach(movie -> {
            movie.setUserModel(null);
        });
        return ResponseEntity.ok(favoriteMovie);
    }

    @GetMapping("/isfav")
    public ResponseEntity<Integer> isMovieUserFavorite(@RequestParam String email, @RequestParam String movieName,
                                                       HttpServletRequest httpServletRequest){
        if(favoriteMovieService.isMovieUserFavoriteByMovieName(movieName, email)){
            return ResponseEntity.ok(1);
        }
        return ResponseEntity.ok(0);
    }


    @PostMapping("/addfavorite")
    public ResponseEntity<Integer> addFavoriteMovieApi(@RequestBody FavoriteMovieAddRequest favoriteMovieAddRequest,
                                              HttpServletRequest httpServletRequest){
        // get user from the request
        Optional<UserModel> userModel = userService.findUserByEmail(favoriteMovieAddRequest.getEmail());
        FavoriteMovie favoriteMovie = new FavoriteMovie();
        if(userModel.isPresent()){
            // create favorite movie instance with the current user
            favoriteMovie.setMovieId(favoriteMovieAddRequest.getMovieId());
            favoriteMovie.setMovieName(favoriteMovieAddRequest.getMovieName());
            favoriteMovie.setMoviePictureUrl(favoriteMovieAddRequest.getMoviePictureUrl());
            favoriteMovie.setMovieVideoUrl(favoriteMovieAddRequest.getMovieVideoUrl());
            favoriteMovie.setMovieMeta(favoriteMovieAddRequest.getMovieMeta());
            favoriteMovie.setUserModel(userModel.get());
            // save favorite movie to db
            favoriteMovieService.addFavoriteMovie(favoriteMovie);
            return ResponseEntity.ok(1);
        }
        return ResponseEntity.ok(0);
    }

    @GetMapping("/removefav")
    public ResponseEntity<Integer> removeFavoriteMovie(@RequestParam String email, @RequestParam String movieName,
                                                       HttpServletRequest httpServletRequest){
        if(favoriteMovieService.removeUserFavoriteMovieByMovieName(movieName, email)){
            return ResponseEntity.ok(1);
        }
        return ResponseEntity.ok(0);
    }
}

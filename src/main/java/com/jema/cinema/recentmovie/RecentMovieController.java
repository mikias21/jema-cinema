package com.jema.cinema.recentmovie;

import com.jema.cinema.user.UserModel;
import com.jema.cinema.user.UserRepository;
import com.jema.cinema.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping(path = "/jema/user/movie/recent")
@AllArgsConstructor
public class RecentMovieController {

    @Autowired
    private RecentMovieService recentMovieService;
    @Autowired
    private UserService userService;

    // cors config
    @Bean
    public WebMvcConfigurer corsConfigureRecentMovie(){
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

    @PostMapping("/add")
    public ResponseEntity<Integer> addRecentMovie(@RequestBody @Validated RecentMovieRequest recentMovieRequest,
                                            HttpServletRequest httpServletRequest){
        Optional<UserModel> userModel = userService.findUserByEmail(recentMovieRequest.getEmail());
        if(userModel.isPresent()){
            // check if movie is saved
            if(!recentMovieService.isMovieSaveByUser(userModel.get(), recentMovieRequest.getMovieName())){
                // set values here
                RecentMovie recentMovie = new RecentMovie();
                recentMovie.setMovieName(recentMovieRequest.getMovieName());
                recentMovie.setMovieImageUrl(recentMovieRequest.getMovieImageUrl());
                recentMovie.setMovieVideoUrl(recentMovieRequest.getMovieVideoUrl());
                recentMovie.setMovieId(recentMovieRequest.getMovieId());
                recentMovie.setUserModel(userModel.get());
                recentMovie.setMeta(recentMovieRequest.getMeta());
                if(recentMovieService.addRecentMovieData(recentMovie)){
                    return ResponseEntity.ok(1);
                }
            }else{
                return ResponseEntity.ok(0);
            }
        }else{
            return ResponseEntity.ok(0);
        }
        return ResponseEntity.ok(0);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getRecentMovie(@RequestParam String email, HttpServletRequest httpServletRequest){
        Optional<UserModel> userModel = userService.findUserByEmail(email);
        List<RecentMovie> recentMovieList = null;
        if(userModel.isPresent()){
            recentMovieList = recentMovieService.getRecentMovieByUser(userModel.get());
            if(recentMovieList.size() > 0){
                recentMovieList.forEach(recentMovie -> recentMovie.setUserModel(null));
                return ResponseEntity.ok(recentMovieList);
            }
        }
        return ResponseEntity.ok(0);
    }

    @GetMapping("/del")
    public ResponseEntity<Integer> removeRecentMovie(@RequestParam String email, @RequestParam Long movieId){
        if(recentMovieService.removeRecentMovie(email, movieId)){
            return ResponseEntity.ok(1);
        }
        return ResponseEntity.ok(0);
    }
}

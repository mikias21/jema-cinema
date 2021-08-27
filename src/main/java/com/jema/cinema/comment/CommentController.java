package com.jema.cinema.comment;

import com.jema.cinema.user.UserModel;
import com.jema.cinema.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/jema/user/movie/comment")
@AllArgsConstructor
public class CommentController {

    private UserService userService;
    private CommentService commentService;

    // cors config
    @Bean
    public WebMvcConfigurer corsConfigureForUserComment(){
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

    @PostMapping("/comment")
    public ResponseEntity<?> addCommentApi(@RequestBody @Validated CommentRequest commentRequest,
                                     HttpServletRequest httpServletRequest){
        // set values to the comment section
        Optional<UserModel> userModel = userService.findUserByEmail(commentRequest.getUserEmail());
        if(userModel.isPresent()){
            Comment comment = new Comment();
            comment.setMovieId(commentRequest.getMovieId());
            comment.setMovieName(commentRequest.getMovieName());
            comment.setMovieImage(commentRequest.getMovieImage());
            comment.setMovieVideo(commentRequest.getMovieVideo());
            comment.setMovieMeta(commentRequest.getMovieMeta());
            comment.setUserModel(userModel.get());
            comment.setRate(commentRequest.getRate());
            comment.setComment(commentRequest.getComment());
            try{
                commentService.addComment(comment);
            }catch (Exception e){
                return ResponseEntity.ok(e.getMessage());
            }

        }
        return ResponseEntity.ok(1);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getMovieCommentsApi(@RequestParam String movieId, HttpServletRequest httpServletRequest){
        List<Comment> comments = commentService.getMovieComments(movieId);
        List<CommentGetResponse> commentGetResponses = new ArrayList<>();
        comments.stream().forEach(comment -> {
            CommentGetResponse commentGetResponse = new CommentGetResponse();
            commentGetResponse.setUserName(comment.getUserModel().getFirstName() + " " + comment.getUserModel().getLastName());
            commentGetResponse.setComment(comment.getComment());
            commentGetResponse.setRating(comment.getRate());
            commentGetResponse.setCommentedDateTime(comment.getCommentedDateTime());
            commentGetResponses.add(commentGetResponse);
        });
        return ResponseEntity.ok(commentGetResponses);
    }

    @GetMapping("/rating")
    public ResponseEntity<?> getMovieAverageRatingApi(@RequestParam String movieId,
                                                      HttpServletRequest httpServletRequest){
        float avgRating = commentService.getMovieAverageRating(movieId);
        return ResponseEntity.ok(avgRating);
    }
}

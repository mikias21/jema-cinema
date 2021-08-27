package com.jema.cinema.comment;

import com.jema.cinema.user.UserModel;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    private String regex = "^(?!\\d+$)(?:[a-zA-Z0-9][a-zA-Z0-9 .@&?!%]*)?$";
    private Pattern pattern = Pattern.compile(regex);


    public List<UserModel> getCommentedUsers(String movieId){
        List<Comment> comments = commentRepository.findByMovieId(movieId);
        List<UserModel> userModels = new ArrayList<>();
        comments.forEach(comment -> {
            userModels.add(comment.getUserModel());
        });
        return userModels;
    }

    public Boolean isMovieCommented(String movieId){
        return commentRepository.findByMovieId(movieId).size() > 0;
    }

    public Boolean hasUserCommented(String userEmail, UserModel userModel){
        return userModel.getEmail().equals(userEmail);
    }

    public Boolean isMovieCommentedByUser(String movieId, String userEmail){
        if(isMovieCommented(movieId)){
            List<UserModel> userModels = getCommentedUsers(movieId);
            UserModel userModel = getCommentedUsers(movieId).stream().
                    filter(user -> user.getEmail().equals(userEmail)).findFirst().orElse(null);
            assert userModel != null;
            return hasUserCommented(userEmail, userModel);
        }
        return false;
    }

    public Boolean isCommentValid(String comment){
        Matcher matcher = pattern.matcher(comment);
        return matcher.matches();
    }
    public void addComment(Comment comment) throws Exception{
        if(isCommentValid(comment.getComment())){
            if(!isMovieCommentedByUser(comment.getMovieId(), comment.getUserModel().getEmail())){
                commentRepository.save(comment);
            }
        }else{
            throw new Exception("letters, numbers and . @ & ? ! % allowed only");
        }
    }

    public List<Comment> getMovieComments(String movieId){
        return commentRepository.findByMovieId(movieId);
    }

    public float getMovieAverageRating(String movieId){
        return commentRepository.findAverageMovieRating(movieId);
    }
}

package com.jema.cinema.recentmovie;

import com.jema.cinema.user.UserModel;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
@AllArgsConstructor
public class RecentMovieService {
    @Autowired
    private RecentMovieRepository recentMovieRepository;

    public boolean addRecentMovieData(RecentMovie recentMovie) {
        // check if the movie is already added the current user
        Optional<RecentMovie> recentMovieOptional = recentMovieRepository.findByMovieName(recentMovie.getMovieName());
        if(recentMovieOptional.isPresent()){
            // check if the user is the same
            UserModel userModel = recentMovieOptional.get().getUserModel();
            if(userModel.equals(recentMovie.getUserModel())){
                return false;
            }
        }
        // add movie here
        recentMovieRepository.save(recentMovie);
        return true;
    }

    public Boolean isMovieSaveByUser(UserModel userModel, String movieName){
        AtomicReference<Boolean> isSaved = new AtomicReference<>(false);
        List<RecentMovie> favoriteMovieList = recentMovieRepository.findByUserModel(userModel);
        favoriteMovieList.forEach(favoriteMovie -> {
            if(favoriteMovie.getMovieName().equals(movieName)){
                isSaved.set(true);
            }
        });
        return isSaved.get();
    }

    public List<RecentMovie> getRecentMovieByUser(UserModel userModel){
        return recentMovieRepository.findByUserModel(userModel);
    }

    public boolean removeRecentMovie(String email, Long movieId){
        Optional<RecentMovie> recentMovieOptional = Optional.of(recentMovieRepository.getById(movieId));
        if(recentMovieOptional.isPresent()){
            // check if the user is right
            if(recentMovieOptional.get().getUserModel().getEmail().equals(email)){
                recentMovieRepository.delete(recentMovieOptional.get());
                return true;
            }
        }
        return false;
    }
}

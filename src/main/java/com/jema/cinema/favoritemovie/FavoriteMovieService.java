package com.jema.cinema.favoritemovie;

import com.jema.cinema.user.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FavoriteMovieService {
    @Autowired
    private FavoriteMovieRepository favoriteMovieRepository;

    public List<FavoriteMovie> getUserFavoriteMovieByUser(UserModel userModel){
        List<FavoriteMovie> favoriteMovie = favoriteMovieRepository.findByUserModel(userModel);
        return favoriteMovie;
    }

    public void addFavoriteMovie(FavoriteMovie favoriteMovie){
        favoriteMovieRepository.save(favoriteMovie);
    }

    public boolean isMovieUserFavorite(String movieId, String email){
        if(movieId.length() > 0){
            Optional<FavoriteMovie> movie = favoriteMovieRepository.findByMovieId(movieId);
            if(movie.isPresent()){
                UserModel userModel = movie.get().getUserModel();
                if(userModel != null){
                    if(userModel.getEmail().equals(email)){
                        return true;
                    }
                }
                return false;
            }
            return false;
        }
        return false;
    }

    public boolean isMovieUserFavoriteByMovieName(String movieName, String email){
        if(movieName.length() > 0){
            Optional<FavoriteMovie> movieOptional = favoriteMovieRepository.findByMovieName(movieName);
            if(movieOptional.isPresent()){
                UserModel userModel = movieOptional.get().getUserModel();
                if(userModel != null){
                    if(userModel.getEmail().equals(email)){
                        return true;
                    }
                }
                return false;
            }
            return false;
        }
        return false;
    }

    public boolean removeUserFavoriteMovie(String movieId, String email){
        if(movieId.length() >= 0){
            Optional<FavoriteMovie> movie = favoriteMovieRepository.findByMovieId(movieId);
            if(movie.isPresent()){
                UserModel userModel = movie.get().getUserModel();
                if(userModel != null){
                    if(userModel.getEmail().equals(email)){
                        favoriteMovieRepository.delete(movie.get());
                        return true;
                    }
                }
                return false;
            }
            return false;
        }
        return false;
    }

    public boolean removeUserFavoriteMovieByMovieName(String movieName, String email){
        if(movieName.length() >= 0){
            Optional<FavoriteMovie> movie = favoriteMovieRepository.findByMovieName(movieName);
            if(movie.isPresent()){
                UserModel userModel = movie.get().getUserModel();
                if(userModel != null){
                    if(userModel.getEmail().equals(email)){
                        favoriteMovieRepository.delete(movie.get());
                        return true;
                    }
                }
                return false;
            }
            return false;
        }
        return false;
    }
}

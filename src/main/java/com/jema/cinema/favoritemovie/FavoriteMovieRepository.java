package com.jema.cinema.favoritemovie;

import com.jema.cinema.user.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteMovieRepository extends JpaRepository<FavoriteMovie, Long> {
    Optional<FavoriteMovie> findById(Long id);
    List<FavoriteMovie> findByUserModel(UserModel userModel);
    Optional<FavoriteMovie> findByMovieId(String movieId);
    Optional<FavoriteMovie> findByMovieName(String movieName);
}

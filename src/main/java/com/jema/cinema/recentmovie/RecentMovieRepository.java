package com.jema.cinema.recentmovie;

import com.jema.cinema.user.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecentMovieRepository extends JpaRepository<RecentMovie, Long> {
    Optional<RecentMovie> findById(Long id);
    Optional<RecentMovie> findByMovieId(String movieId);
    List<RecentMovie> findByUserModel(UserModel userModel);
    Optional<RecentMovie> findByMovieName(String movieName);
}

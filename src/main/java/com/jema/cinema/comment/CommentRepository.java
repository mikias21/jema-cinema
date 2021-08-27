package com.jema.cinema.comment;

import com.jema.cinema.user.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByMovieName(String movieName);
    List<Comment> findByMovieId(String movieId);
    List<Comment> findByUserModel(UserModel userModel);
    Optional<Comment> findById(Long id);
    @Query("SELECT AVG(c.rate) FROM Comment c WHERE c.movieId = ?1")
    float findAverageMovieRating(String movieId);
}

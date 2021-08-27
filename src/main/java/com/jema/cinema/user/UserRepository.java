package com.jema.cinema.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findByEmail(String email);
    Optional<UserModel> findByDeviceIp(String ip);
    Optional<UserModel> findByUserId(String userId);
}

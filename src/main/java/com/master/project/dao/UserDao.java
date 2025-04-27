package com.master.project.dao;

import com.master.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@EnableJpaRepositories
@Repository
public interface UserDao extends JpaRepository<User, String> {
    Optional<User> findByUserIDAndPassword(String userId, String password);
}

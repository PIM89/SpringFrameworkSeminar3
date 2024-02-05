package ru.gb.springdemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.gb.springdemo.model.UserApp;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserApp, Long> {
    Optional<UserApp> findUserByName(String name);
    void deleteByName(String name);
    boolean existsByName(String name);
}

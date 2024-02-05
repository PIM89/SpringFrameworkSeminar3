package ru.gb.springdemo.service;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.gb.springdemo.model.UserApp;
import ru.gb.springdemo.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Data
@NoArgsConstructor
public class UserService {
    private UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public Optional<UserApp> findUserByName(String name) {
        return userRepository.findUserByName(name);
    }
    public void deleteByName(String name) {
        userRepository.deleteByName(name);
    }
    public void saveUser(UserApp user) {
        userRepository.save(user);
    }

}

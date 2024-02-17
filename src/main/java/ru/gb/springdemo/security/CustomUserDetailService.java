package ru.gb.springdemo.security;


import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.gb.springdemo.model.Role;
import ru.gb.springdemo.model.UserApp;
import ru.gb.springdemo.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       Optional<UserApp> currentUser = userRepository.findUserByName(username);
        if (currentUser == null) {
            throw new UsernameNotFoundException(username);
        }
        List<GrantedAuthority> authorityList = new ArrayList<>();
        for (Role role : currentUser.get().getRoleList()) {
            authorityList.add(new SimpleGrantedAuthority(role.getName()));
        }
        return new User(currentUser.get().getName(), currentUser.get().getPassword(), authorityList);

    }

    @PostConstruct
    public void addAdmin() {
        if (!userRepository.existsByName("admin")) {
            UserApp user = new UserApp();
            user.setName("admin");
            user.setPassword(new BCryptPasswordEncoder().encode("admin"));
            user.setRoleList(List.of(new Role("admin")));
            userRepository.save(user);
            userRepository.flush();
        }
    }
}

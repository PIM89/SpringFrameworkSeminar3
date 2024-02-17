package ru.gb.springdemo.security;

import com.fasterxml.jackson.databind.ser.std.StdKeySerializers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.authorizeHttpRequests(conf -> conf
                        .requestMatchers("/ui/issues/**").hasAuthority("admin")
                        .requestMatchers("/reader/add/").hasAuthority("admin")
                        .requestMatchers("/ui/readers/**").hasAuthority("reader")
                        .requestMatchers("/ui/books/**").authenticated()
                        .anyRequest().permitAll())
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(Customizer.withDefaults())
                .build();
    }
}

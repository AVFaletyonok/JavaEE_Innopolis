package org.example.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final DataSource dataSource;

    @Bean
    public InMemoryUserDetailsManager user(PasswordEncoder encoder) {
        var manager = new InMemoryUserDetailsManager();
        manager.createUser(new User("user1",  encoder.encode("123456"),
                List.of(new SimpleGrantedAuthority("USER"))));
        return manager;
    }

//    @Bean
//    public JdbcUserDetailsManager user(PasswordEncoder encoder) {
//        var manager = new JdbcUserDetailsManager(dataSource);
//        manager.createUser(new User("user1",  encoder.encode("123456"),
//                                    List.of(new SimpleGrantedAuthority("USER"))));
//        return manager;
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Security settings
        http
                .csrf().disable()  // for post query to signup for courses without authorization
                .authorizeHttpRequests(authorization ->
                        authorization
                                .requestMatchers("/api/v1/students/lk/{id}").hasAuthority("USER")
                                .anyRequest().permitAll())
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

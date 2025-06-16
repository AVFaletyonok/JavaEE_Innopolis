package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Конфигурация безопасности по форме с использованием Granted Authority
 * и настройкой доступа к endpoint-ам для ролей ADMIN, USER, authenticated.
 *
 * @author Faletyonok Alexander
 * @version 1.0
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    /**
     * Bean настраивает FilterChain для доступа к endpoint-ам для ролей ADMIN, USER, authenticated.
     *
     * @param http запрос
     * @return FilterChain с настройками доступа для ролей ADMIN, USER, authenticated.
     * @throws Exception исключение csrf
     */
    @Bean
    public SecurityFilterChain getSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.DELETE, "/api/tasks/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/tasks").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/tasks/{id}").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/tasks", "/api/tasks/{id}").authenticated()
                        .requestMatchers("/api/tasks/home").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    /**
     * Bean настраивает список пользователей для доступа к endpoint-ам: admin, user, viewer.
     *
     * @param encoder кодировщик паролей.
     * @return менеджер пользователей с добавленным списком пользователей.
     */
    @Bean
    public InMemoryUserDetailsManager user(PasswordEncoder encoder) {
        var manager = new InMemoryUserDetailsManager();
        manager.createUser(User.builder()
                .username("admin")
                .password(encoder.encode("adminpass"))
                .roles("ADMIN")
                .build());
        manager.createUser(User.builder()
                .username("user")
                .password(encoder.encode("userpass"))
                .roles("USER")
                .build());
        manager.createUser(User.builder()
                .username("viewer")
                .password(encoder.encode("viewerpass"))
                .roles("VIEWER")
                .build());
        return manager;
    }

    /**
     * Выбор кодирощика BCryptPasswordEncoder.
     *
     * @return кодировщик BCryptPasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

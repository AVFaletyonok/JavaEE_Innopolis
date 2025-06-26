package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain getSecurityFilterChain(HttpSecurity http,
                                                      CustomBasicAuthenticationEntryPoint customBasicAuthenticationEntryPoint)
//            , RequestLoggingFilter requestLoggingFilter)
            throws Exception {

        http.csrf(csrf -> csrf.disable())
                // Second var to create a request filter
//                .addFilterBefore(requestLoggingFilter, BasicAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.DELETE, "/api/tasks/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/tasks").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/tasks/{id}").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/tasks", "/api/tasks/{id}").authenticated()
                        .requestMatchers("/api/tasks/home").permitAll()
                        .anyRequest().authenticated()
                )
//                .formLogin((formLogin) -> {
//                    formLogin.successForwardUrl("/api/tasks")
//                            .failureForwardUrl("/login")
//                            .loginPage("/login");
//                })
//                .formLogin(Customizer.withDefaults())
//                .httpBasic(Customizer.withDefaults());
//                .httpBasic(httpBasic -> httpBasic.realmName("HiddenRealm"));
                .httpBasic(httpBasic -> httpBasic.authenticationEntryPoint(customBasicAuthenticationEntryPoint));
        return http.build();
    }

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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

package com.example.demo8.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http


                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/users").permitAll()
                                .requestMatchers("/login").permitAll()
                                .requestMatchers("/register").hasRole("0")
                                .requestMatchers("/abc").permitAll()
                                .requestMatchers("/users").hasRole("0")
                                .requestMatchers("/giao").hasRole("0")
                                .requestMatchers("/delete").hasRole("0")
                                .requestMatchers("/saveUsers").hasRole("0")
                                .requestMatchers("/giao").hasRole("1")
                                .requestMatchers("/create-challenge").hasRole("0")
                                .requestMatchers("/submit-answer").hasRole("1")
                                .requestMatchers("/downloadhomework").hasRole("1")
                                .requestMatchers("/downloadfile").hasRole("1")
                                .requestMatchers("/users").authenticated()
                                .requestMatchers("/**").authenticated()

                )
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/login")
                                .permitAll()
                                .defaultSuccessUrl("/postLogin", true)
                                .failureUrl("/login?error=true")
                )
                .logout(logout ->
                        logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/login?logout=true")
                                .permitAll()
                )
//                .csrf(csrf -> csrf.disable()
//                )
;

        return http.build();
    }



    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

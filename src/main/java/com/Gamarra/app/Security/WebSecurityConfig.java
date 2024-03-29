package com.Gamarra.app.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;

    public WebSecurityConfig(UserDetailsService userDetailsService, BCryptPasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/", "/login").permitAll()
                .requestMatchers("/empleados/**").hasRole("ADMINISTRADOR")
                .requestMatchers("/pedidos/**", "/ventas/**", "/clientes/**", "/servicios/**", "/home").hasAnyRole("ADMINISTRADOR", "USUARIO")
                .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                .loginPage("/login")
                .defaultSuccessUrl("/home")
                .permitAll()
                )
                .logout((logout) -> logout.permitAll());

        return http.build();
    }
}

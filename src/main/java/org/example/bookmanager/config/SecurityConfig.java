package org.example.bookmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/h2-console/**").permitAll() // H2 콘솔 허용
                        .anyRequest().authenticated()
                )
                .formLogin(withDefaults())
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**") // CSRF 예외 추가
                )
                .headers(headers -> headers
                        .frameOptions(frame -> frame
                                .sameOrigin() //iframe 허용 (H2 콘솔은 iframe 사용 하는데 시큐리티가 막음)
                        )
                );

        return http.build();
    }

    @Bean
    public UserDetailsService users() {
        UserDetails user = User.builder()
                .username("admin")
                .password("{noop}1234")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}

package com.example.springbootarticles.security;

import com.example.springbootarticles.services.JwtAuthenticationEntryPoint;
import com.example.springbootarticles.services.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationEntryPoint point;

    @Autowired
    private JwtAuthenticationFilter filter;

    private static final String[] AUTH_WHITE_LIST = {
            // API routes
            "/api/profiles/{username}",
            "/api/articles",
            "/api/articles/{slug}",
            "/api/articles/{slug}/comments",
            "/api/tags",

            // Swagger UI
            "/v3/api-docs",
            "/swagger-ui.html",
            "/swagger-ui/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        RequestMatcher apiMatcher = new AntPathRequestMatcher("/api/**");
        http.csrf(AbstractHttpConfigurer::disable)
                .formLogin(form -> form.loginPage("/api/users").permitAll())
                .logout(logout -> logout.logoutUrl("/api/logout")
                        .clearAuthentication(true)
                        .invalidateHttpSession(true)
                        .logoutSuccessUrl("/api/users/login")
                        .permitAll())
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .antMatchers(AUTH_WHITE_LIST).permitAll()
                        .requestMatchers(apiMatcher).authenticated()
                )
                .exceptionHandling(e -> e.authenticationEntryPoint(this.point))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
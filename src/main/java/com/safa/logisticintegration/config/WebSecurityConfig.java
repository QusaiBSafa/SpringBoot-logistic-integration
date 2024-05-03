package com.safa.logisticintegration.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtFilter jwtFilter;
    private final UserDetailsService jwtUserDetailsService;

    @Autowired
    public WebSecurityConfig(
            JwtAuthenticationEntryPoint authenticationEntryPoint,
            JwtFilter jwtFilter,
            UserDetailsService jwtUserDetailsService) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.jwtFilter = jwtFilter;
        this.jwtUserDetailsService = jwtUserDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf()
                .disable().exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().
                authorizeHttpRequests().requestMatchers("/v3/api-docs/**",
                        "/v2/api-docs",
                        "/swagger-ui.html/**",
                        "/swagger*/**",
                        "/configuration/**",
                        "/swagger-ui/index.html/**",
                        "/webjars/**", "/actuator/**", "/api/transcorp/**", "/api/v1/shipments/pull-updates", "/api/v1/tawseel").permitAll()
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .anyRequest().authenticated();

        // Add a filter to validate the tokens with every request
        httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }


}

package com.sarz.electronic.store.config;

import com.sarz.electronic.store.Security.JWTAuthenticationEntryPoint;
import com.sarz.electronic.store.Security.JWTAuthenticationFilter;
import com.sarz.electronic.store.Security.JWTHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JWTAuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private JWTHelper jwtHelper;
    @Autowired
    private JWTAuthenticationFilter authenticationFilter;

    private static final String[] PUBLIC_URLS={
        "/swagger-ui/**",
            "/webjars/**",
            "/swagger-resources/**",
            "/v3/api-docs/**"
    };

    //    @Bean
//    public UserDetailsService userDetailsService(){
//
//        UserDetails Admin = User.builder()
//                .username("SARVESH")
//                .password(passwordEncoder().encode("sarvesh"))
//                .roles("ADMIN")
//                .build();
//
//        UserDetails Normal = User.builder()
//                .username("Yash")
//                .password(passwordEncoder().encode("yash"))
//                .roles("Normal")
//                .build();
//
//        return new InMemoryUserDetailsManager(Admin,Normal);
//    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(this.userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.
                csrf(AbstractHttpConfigurer::disable).
                cors(AbstractHttpConfigurer::disable).
                authorizeHttpRequests(auth -> auth.requestMatchers("/auth/login").permitAll()).
                authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.POST, "/users").permitAll()).
                authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")).
                authorizeHttpRequests(auth -> auth.requestMatchers("/auth/google").permitAll()).
                authorizeHttpRequests(auth->auth.requestMatchers(PUBLIC_URLS).permitAll()).
                authorizeHttpRequests(auth -> auth.anyRequest().authenticated()).
                exceptionHandling(ex -> ex.authenticationEntryPoint(authenticationEntryPoint)).
                sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }
}
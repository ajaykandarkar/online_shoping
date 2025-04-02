package com.example.demo.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;



@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	 @Bean
	 AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	 }
	 
	 @Bean
	 AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	 }
	 
	 @Bean
	 SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
			  httpSecurity
	          .cors(cors -> cors.configurationSource(corsConfigurationSource()))   
	          .csrf(csrf -> csrf.disable()) 										
	          .authorizeHttpRequests(authorize ->
	                  authorize.requestMatchers("/login",
	                		  	    "/createUser",
	                		  	    "/api/user", 
	                		  	    "/api/user/login",
	                		  	    "/api/user/hello",
	                		  	    "/swagger-ui/**",
	                		  	    "/v3/api-docs/**",
	                		  	    "/api/forgotPassword/**")
	                          .permitAll()
	                          .anyRequest()
				  .authenticated()
	          )
	          .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	          .authenticationProvider(authenticationProvider())
	          .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		  return httpSecurity.build();
	 }
	 
	 @Bean
	 CorsConfigurationSource corsConfigurationSource() {
	        CorsConfiguration configuration = new CorsConfiguration();
	        configuration.setAllowedOrigins(List.of("http://localhost:5173"));                   
	        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); 
	        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));           
	        configuration.setAllowCredentials(true);                                             
	        configuration.setExposedHeaders(List.of("Authorization"));                           

	        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	        source.registerCorsConfiguration("/**", configuration);
	        return source;
	 }
}

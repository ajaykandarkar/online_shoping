package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import com.example.demo.dto.GetResponseDto;
import com.example.demo.dto.LoginDto;
import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.UserDto;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.repository.DemoRepository;
import com.example.demo.security.JwtTokenProvider;

@Service
public class UserService {

    @Autowired
    private DemoRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public String hello() {
        return "Hello";
    }

    public List<GetResponseDto> getAll() {
        return repo.findAll().stream().map(this::convertToGetResponseDto).collect(Collectors.toList());
    }
    
    public GetResponseDto convertToGetResponseDto(User user) {
    	GetResponseDto dto = GetResponseDto.builder()
    			.name(user.getName())
    			.age(user.getAge())
    			.salary(user.getSalary())
    			.email(user.getEmail())
    			.build();
    	return dto;
    }

    public void updateSalary(double salary, int id) {
        User user = repo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));
        user.setSalary(salary);
        repo.save(user);
    }

    public boolean createUser(UserDto userDto) {
        Optional<User> gmail =repo.findByEmail(userDto.getEmail());
         
         if(!gmail.isPresent()) {
        	 User user = User.builder()
                     .name(userDto.getName())
                     .age(userDto.getAge())
                     .salary(userDto.getSalary())
                     .email(userDto.getEmail())
                     .password(passwordEncoder.encode(userDto.getPassword()))
                     .role(Role.valueOf("ROLE_USER"))
                     .build();
             repo.save(user);
             return true;
         }
        else{
            return false;
        }
    }

    public LoginResponse login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));

        String token = jwtTokenProvider.generateToken(authentication);
        return new LoginResponse("success", "Login successful", token, loginDto.getEmail(), LocalDateTime.now().toString());
    }


}

package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public List<User> getAll() {
        return repo.findAll();
    }

    public void updateSalary(double salary, int id) {
        User user = repo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));
        user.setSalary(salary);
        repo.save(user);
    }

    public void createUser(UserDto userDto) {
        if (repo.findByEmail(userDto.getEmail()) != null) {
            throw new UserNotFoundException("User with email " + userDto.getEmail() + " already exists");
        }

        User user = User.builder()
                .name(userDto.getName())
                .age(userDto.getAge())
                .salary(userDto.getSalary())
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .role(Role.valueOf("ROLE_USER"))
                .build();
        repo.save(user);
    }

    public LoginResponse login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));

        String token = jwtTokenProvider.generateToken(authentication);
        return new LoginResponse("success", "Login successful", token, loginDto.getEmail(), LocalDateTime.now().toString());
    }


}

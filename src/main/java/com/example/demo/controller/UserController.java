package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.LoginDto;
import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.UpddateSalaryResponse;
import com.example.demo.dto.UserDto;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("{\"message\": \"" + userService.hello() + "\"}");
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<User>> getAll() {
        List<User> users = userService.getAll();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/updatesalary/{salary}/{id}")
    public ResponseEntity<UpddateSalaryResponse> updateSalary(@PathVariable("salary") double salary, @PathVariable("id") int id) {
        userService.updateSalary(salary, id);
        return ResponseEntity.ok(new UpddateSalaryResponse("success", "Salary update successful", LocalDateTime.now().toString()));
    }

    @PostMapping("/createUser")
    public ResponseEntity<String> createUser(@RequestBody UserDto userDto) {
        userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginDto loginDto) {
        LoginResponse response = userService.login(loginDto);
        return ResponseEntity.ok(response);
    }

}

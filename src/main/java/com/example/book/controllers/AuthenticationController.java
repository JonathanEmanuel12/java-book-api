package com.example.book.controllers;

import java.io.Console;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.book.dtos.LoginUserDto;
import com.example.book.dtos.RegisterUserDto;
import com.example.book.models.User;
import com.example.book.repositories.UserRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public AuthenticationController(AuthenticationManager authenticationManager, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity login(
        @RequestBody() @Valid LoginUserDto userDto
    ) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(userDto.email(), userDto.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        return ResponseEntity.ok().body(auth);
    }

    @PostMapping("/register")
    public ResponseEntity register(
        @RequestBody() @Valid RegisterUserDto userDto
    ) {
        if(this.userRepository.findByEmail(userDto.email()) != null) return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(userDto.password());
        System.out.println(encryptedPassword);
        User user = new User(userDto.email(), encryptedPassword, userDto.name());
        this.userRepository.save(user);

        return ResponseEntity.ok().build();
    }
}

package com.example.book.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.book.dtos.LoginResponseDto;
import com.example.book.dtos.LoginUserDto;
import com.example.book.dtos.RegisterUserDto;
import com.example.book.models.User;
import com.example.book.repositories.UserRepository;
import com.example.book.services.TokenService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private TokenService tokenService;

    public AuthenticationController(AuthenticationManager authenticationManager, UserRepository userRepository, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
        @RequestBody() @Valid LoginUserDto userDto
    ) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(userDto.email(), userDto.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((User) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDto(token));
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

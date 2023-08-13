package com.personalProject.reddit.controller;

import com.personalProject.reddit.dto.AuthenticationResponse;
import com.personalProject.reddit.dto.LoginRequest;
import com.personalProject.reddit.dto.RefreshTokenRequest;
import com.personalProject.reddit.dto.RegisterRequest;
import com.personalProject.reddit.service.AuthService;
import com.personalProject.reddit.service.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController  {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody RegisterRequest registerRequest){
        authService.signUp(registerRequest);

        return new ResponseEntity<>("User Registration is Successful", HttpStatus.OK);
    }

    @GetMapping("/accountverification/{token}")
    public ResponseEntity<String> verifyToken(@PathVariable String token){
        authService.verifyAccount(token);

        return new ResponseEntity<>("User account activated successfully", HttpStatus.OK);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest){
        return authService.login(loginRequest);
    }

    @PostMapping("/refresh/token")
    public AuthenticationResponse refreshTokens(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest){
        return authService.refreshToken(refreshTokenRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest){
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.status(HttpStatus.OK).body("Refresh Token Deleted Successfully!!");
    }





}

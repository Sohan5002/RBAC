package com.example.rbac.controller;

import com.example.controller.AuthenticationService;
import com.example.rbac.model.AuthResponse;
import com.example.rbac.model.User;
import com.example.rbac.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestParam String username, 
                                        @RequestParam String password, 
                                        @RequestParam String email) {
        User user = authenticationService.registerUser(username, password, email);
        return ResponseEntity.ok("User registered successfully: " + user.getUsername());
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestParam String username, 
                                     @RequestParam String password) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, password)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken(username);
        
        List<String> roles = authentication.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new AuthResponse(jwt, refreshToken, username, roles));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestParam String refreshToken) {
        if (tokenProvider.validateToken(refreshToken)) {
            String username = tokenProvider.getUsernameFromToken(refreshToken);
            String newToken = tokenProvider.generateToken(
                SecurityContextHolder.getContext().getAuthentication()
            );
            String newRefreshToken = tokenProvider.generateRefreshToken(username);
            
            return ResponseEntity.ok(new AuthResponse(
                newToken, 
                newRefreshToken, 
                username,
                SecurityContextHolder.getContext().getAuthentication()
                    .getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList())
            ));
        }
        return ResponseEntity.badRequest().body("Invalid refresh token");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logged out successfully");
    }
}

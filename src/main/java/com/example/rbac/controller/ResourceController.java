package com.example.rbac.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ResourceController {

    @GetMapping("/public")
    public ResponseEntity<?> getPublicResource() {
        return ResponseEntity.ok("This is a public resource");
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getUserResource() {
        return ResponseEntity.ok("This is a user resource");
    }

    @GetMapping("/moderator")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<?> getModeratorResource() {
        return ResponseEntity.ok("This is a moderator resource");
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAdminResource() {
        return ResponseEntity.ok("This is an admin resource");
    }

    @GetMapping("/secured")
    @PreAuthorize("hasAnyRole('USER', 'MODERATOR', 'ADMIN')")
    public ResponseEntity<?> getSecuredResource() {
        return ResponseEntity.ok("This is a secured resource accessible by authenticated users");
    }
}

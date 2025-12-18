package com.example.cheongyakassist.auth.controller;

import com.example.cheongyakassist.auth.service.AuthService;
import com.example.cheongyakassist.user.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@RequestBody User user) {

        User createdUser = authService.register(
                user.getEmail(),
                user.getPassword(),
                user.getName()
        );

        return ResponseEntity.ok()
                .body(createdUser);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> request, HttpSession session){
        String email = request.get("email");
        String password = request.get("password");

        User user = authService.login(email, password);

        session.setAttribute("loginUser", user);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "login_success");
        response.put("data", user);

        return ResponseEntity.ok(response);
    }

}

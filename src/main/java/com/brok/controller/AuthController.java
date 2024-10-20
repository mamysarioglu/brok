package com.brok.controller;

import com.brok.dto.RegisterDTO;
import com.brok.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/auth")
public class AuthController {
    private final UserService userService;


    @PostMapping("/register")
    ResponseEntity<String> register(@Valid @RequestBody RegisterDTO dto) throws RuntimeException {
        userService.save(dto);
        return ResponseEntity.ok().body("success");
    }
}

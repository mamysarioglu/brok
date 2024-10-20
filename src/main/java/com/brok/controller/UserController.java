package com.brok.controller;

import com.brok.entity.User;
import com.brok.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {
    private final UserService userService;
    @GetMapping
    public ResponseEntity<List<User>> list(){
     return ResponseEntity.ok( userService.getAll());
    }
}

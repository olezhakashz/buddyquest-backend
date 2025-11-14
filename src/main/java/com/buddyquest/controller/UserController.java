package com.buddyquest.controller;

import com.buddyquest.dto.CreateUserRequest;
import com.buddyquest.dto.UserSummaryResponse;
import com.buddyquest.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserSummaryResponse createUser(@RequestBody CreateUserRequest request) {
        return userService.createUser(request);
    }

    @GetMapping("/{id}")
    public UserSummaryResponse getUser(@PathVariable UUID id) {
        return userService.getUser(id);
    }
}

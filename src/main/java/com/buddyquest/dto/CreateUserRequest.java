package com.buddyquest.dto;

public record CreateUserRequest(
        String email,
        String displayName,
        String bio
) {}

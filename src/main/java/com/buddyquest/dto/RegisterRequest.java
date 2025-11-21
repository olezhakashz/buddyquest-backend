package com.buddyquest.dto;

public record RegisterRequest(
        String email,
        String displayName,
        String password,
        String bio
) {}

package com.buddyquest.dto;

public record LoginRequest(
        String email,
        String password
) {}

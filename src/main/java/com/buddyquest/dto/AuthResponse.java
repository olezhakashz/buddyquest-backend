package com.buddyquest.dto;

public record AuthResponse(
        String token,
        UserSummaryResponse user
) {}

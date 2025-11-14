package com.buddyquest.dto;

import java.util.UUID;

public record UserSummaryResponse(
        UUID id,
        String email,
        String displayName,
        String bio,
        int points
) {}

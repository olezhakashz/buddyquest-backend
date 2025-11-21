package com.buddyquest.dto;

import java.util.UUID;

public record LeaderboardEntryResponse(
        UUID userId,
        String displayName,
        int points
) {}

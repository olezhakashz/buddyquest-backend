package com.buddyquest.dto;

import java.util.List;
import java.util.UUID;

public record MatchResponse(
        UUID userId,
        String displayName,
        List<String> commonInterests,
        int score
) {}


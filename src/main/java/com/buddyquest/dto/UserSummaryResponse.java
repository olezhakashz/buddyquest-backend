package com.buddyquest.dto;

import java.util.List;
import java.util.UUID;

public record UserSummaryResponse(
        UUID id,
        String email,
        String displayName,
        String bio,
        int points,
        List<String>interests){}

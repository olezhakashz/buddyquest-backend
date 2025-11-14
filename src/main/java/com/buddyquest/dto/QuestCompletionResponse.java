package com.buddyquest.dto;

import java.util.UUID;

public record QuestCompletionResponse(
        UUID userId,
        UUID questId,
        String questTitle,
        int gainedXp,
        int totalPoints
) {}

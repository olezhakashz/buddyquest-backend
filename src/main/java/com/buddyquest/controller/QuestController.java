package com.buddyquest.controller;

import com.buddyquest.dto.QuestCompletionResponse;
import com.buddyquest.model.Quest;
import com.buddyquest.service.QuestService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class QuestController {

    private final QuestService questService;

    public QuestController(QuestService questService) {
        this.questService = questService;
    }

    @GetMapping("/quests")
    public List<Quest> getQuests() {
        return questService.getAllQuests();
    }

    @PostMapping("/users/{userId}/quests/{questId}/complete")
    public QuestCompletionResponse completeQuest(
            @PathVariable UUID userId,
            @PathVariable UUID questId
    ) {
        return questService.completeQuest(userId, questId);
    }
}

package com.buddyquest.service;

import com.buddyquest.dto.QuestCompletionResponse;
import com.buddyquest.model.Achievement;
import com.buddyquest.model.PointLedger;
import com.buddyquest.model.Quest;
import com.buddyquest.model.User;
import com.buddyquest.repository.AchievementRepository;
import com.buddyquest.repository.PointLedgerRepository;
import com.buddyquest.repository.QuestRepository;
import com.buddyquest.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class QuestService {

    private final QuestRepository questRepository;
    private final UserRepository userRepository;
    private final PointLedgerRepository pointLedgerRepository;
    private final AchievementRepository achievementRepository;

    public QuestService(QuestRepository questRepository,
                        UserRepository userRepository,
                        PointLedgerRepository pointLedgerRepository,
                        AchievementRepository achievementRepository) {
        this.questRepository = questRepository;
        this.userRepository = userRepository;
        this.pointLedgerRepository = pointLedgerRepository;
        this.achievementRepository = achievementRepository;
    }

    @PostConstruct
    public void seedDefaultQuests() {
        if (questRepository.count() == 0) {
            questRepository.saveAll(List.of(
                    new Quest("Meet 1 new friend on campus", 50),
                    new Quest("Attend a campus event this week", 80),
                    new Quest("Study together with someone new", 70),
                    new Quest("Join a club or community", 100)
            ));
        }
    }

    public List<Quest> getAllQuests() {
        return questRepository.findAll();
    }

    @Transactional
    public QuestCompletionResponse completeQuest(UUID userId, UUID questId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Quest quest = questRepository.findById(questId)
                .orElseThrow(() -> new IllegalArgumentException("Quest not found"));

        // create achievement record
        Achievement achievement = new Achievement(
                user.getId(),
                quest.getId(),
                "Completed quest: " + quest.getTitle()
        );
        achievementRepository.save(achievement);

        // update points
        PointLedger ledger = pointLedgerRepository.findById(user.getId())
                .orElseGet(() -> new PointLedger(user.getId()));

        ledger.addPoints(quest.getXp());
        pointLedgerRepository.save(ledger);

        return new QuestCompletionResponse(
                user.getId(),
                quest.getId(),
                quest.getTitle(),
                quest.getXp(),
                ledger.getPoints()
        );
    }
}

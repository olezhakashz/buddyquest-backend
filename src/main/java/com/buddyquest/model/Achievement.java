package com.buddyquest.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.UUID;

@Entity
@Table(name = "achievements")
public class Achievement {

    @Getter
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "quest_id")
    private UUID questId;

    protected Achievement() {
    }

    public Achievement(UUID userId, UUID questId, String title) {
        this.userId = userId;
        this.questId = questId;
        this.title = title;
    }

}

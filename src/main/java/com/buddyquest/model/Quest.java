package com.buddyquest.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "quests")
public class Quest {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false)
    private int xp;

    protected Quest() {
    }

    public Quest(String title, int xp) {
        this.title = title;
        this.xp = xp;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getXp() {
        return xp;
    }
}

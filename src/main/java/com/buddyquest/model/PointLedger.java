package com.buddyquest.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "point_ledger")
public class PointLedger {

    @Id
    @Column(name = "user_id")
    private UUID userId;

    @Column(nullable = false)
    private int points = 0;

    protected PointLedger() {
    }

    public PointLedger(UUID userId) {
        this.userId = userId;
        this.points = 0;
    }

    public UUID getUserId() {
        return userId;
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int delta) {
        this.points += delta;
    }
}

package com.buddyquest.model;

import jakarta.persistence.*;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue
    private UUID id;

    @Setter
    @Column(columnDefinition = "text")
    private String bio;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    protected Profile() {
    }

    public Profile(UUID userId, String bio) {
        this.userId = userId;
        this.bio = bio;
    }

    public UUID getId() {
        return id;
    }

    public String getBio() {
        return bio;
    }

    public UUID getUserId() {
        return userId;
    }

}

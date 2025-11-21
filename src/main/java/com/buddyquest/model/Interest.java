package com.buddyquest.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "interests")
public class Interest {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true, length = 120)
    private String name;

    protected Interest() {
        // for JPA
    }

    public Interest(String name) {
        this.name = name.toLowerCase(); // нормализуем
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}


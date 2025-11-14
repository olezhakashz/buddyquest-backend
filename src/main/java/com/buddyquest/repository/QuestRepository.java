package com.buddyquest.repository;

import com.buddyquest.model.Quest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface QuestRepository extends JpaRepository<Quest, UUID> {
}

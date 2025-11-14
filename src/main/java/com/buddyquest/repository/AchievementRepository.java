package com.buddyquest.repository;

import com.buddyquest.model.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AchievementRepository extends JpaRepository<Achievement, UUID> {
}

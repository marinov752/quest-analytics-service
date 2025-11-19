package com.questanalytics.repository;

import com.questanalytics.domain.entity.QuestAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuestAnalyticsRepository extends JpaRepository<QuestAnalytics, UUID> {
    Optional<QuestAnalytics> findByUserId(UUID userId);
}


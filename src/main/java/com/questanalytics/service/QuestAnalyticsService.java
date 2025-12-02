package com.questanalytics.service;

import com.questanalytics.domain.entity.QuestAnalytics;
import com.questanalytics.repository.QuestAnalyticsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class QuestAnalyticsService {

    private static final Logger logger = LoggerFactory.getLogger(QuestAnalyticsService.class);
    private final QuestAnalyticsRepository questAnalyticsRepository;

    public QuestAnalyticsService(QuestAnalyticsRepository questAnalyticsRepository) {
        this.questAnalyticsRepository = questAnalyticsRepository;
    }

    @Transactional
    public void recordQuestCompletion(UUID userId, UUID questId, Long experiencePoints) {
        logger.info("Recording quest completion for user {}: quest {}, XP {}", userId, questId, experiencePoints);
        
        Optional<QuestAnalytics> analyticsOpt = questAnalyticsRepository.findByUserId(userId);
        QuestAnalytics analytics;
        
        if (analyticsOpt.isPresent()) {
            analytics = analyticsOpt.get();
        } else {
            analytics = new QuestAnalytics();
            analytics.setUserId(userId);
            analytics.setTotalExperienceEarned(0L);
            analytics.setTotalQuestsCompleted(0);
            analytics.setCurrentLevel(1);
        }
        
        analytics.setQuestId(questId);
        analytics.setTotalExperienceEarned(analytics.getTotalExperienceEarned() + experiencePoints);
        analytics.setTotalQuestsCompleted(analytics.getTotalQuestsCompleted() + 1);
        analytics.setLastUpdated(LocalDateTime.now());
        
        questAnalyticsRepository.save(analytics);
        logger.info("Quest completion recorded successfully");
    }

    @Transactional
    public void updateUserStatistics(UUID userId, Long totalExperience, Integer level, Integer questsCompleted) {
        logger.info("Updating user statistics for user {}: level {}, XP {}, quests {}", userId, level, totalExperience, questsCompleted);
        
        Optional<QuestAnalytics> analyticsOpt = questAnalyticsRepository.findByUserId(userId);
        QuestAnalytics analytics;
        
        if (analyticsOpt.isPresent()) {
            analytics = analyticsOpt.get();
        } else {
            analytics = new QuestAnalytics();
            analytics.setUserId(userId);
        }
        
        analytics.setTotalExperienceEarned(totalExperience);
        analytics.setCurrentLevel(level);
        analytics.setTotalQuestsCompleted(questsCompleted);
        analytics.setLastUpdated(LocalDateTime.now());
        
        questAnalyticsRepository.save(analytics);
        logger.info("User statistics updated successfully");
    }

    public Map<String, Object> getAnalyticsData(UUID userId) {
        logger.info("Retrieving analytics data for user {}", userId);
        
        Optional<QuestAnalytics> analyticsOpt = questAnalyticsRepository.findByUserId(userId);
        
        Map<String, Object> data = new HashMap<>();
        if (analyticsOpt.isPresent()) {
            QuestAnalytics analytics = analyticsOpt.get();
            data.put("totalExperienceEarned", analytics.getTotalExperienceEarned());
            data.put("totalQuestsCompleted", analytics.getTotalQuestsCompleted());
            data.put("currentLevel", analytics.getCurrentLevel());
            data.put("lastUpdated", analytics.getLastUpdated());
        } else {
            data.put("totalExperienceEarned", 0L);
            data.put("totalQuestsCompleted", 0);
            data.put("currentLevel", 1);
            data.put("lastUpdated", LocalDateTime.now());
        }
        
        return data;
    }

    @Transactional
    public void deleteAnalyticsData(UUID userId) {
        logger.info("Deleting analytics data for user {}", userId);
        
        Optional<QuestAnalytics> analyticsOpt = questAnalyticsRepository.findByUserId(userId);
        if (analyticsOpt.isPresent()) {
            questAnalyticsRepository.delete(analyticsOpt.get());
            logger.info("Analytics data deleted successfully for user {}", userId);
        } else {
            logger.warn("No analytics data found for user {}", userId);
        }
    }
}


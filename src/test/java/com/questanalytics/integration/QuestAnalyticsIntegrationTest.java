package com.questanalytics.integration;

import com.questanalytics.domain.entity.QuestAnalytics;
import com.questanalytics.repository.QuestAnalyticsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class QuestAnalyticsIntegrationTest {

    @Autowired
    private QuestAnalyticsRepository questAnalyticsRepository;

    @Test
    void testSaveAndFindQuestAnalytics() {
        UUID userId = UUID.randomUUID();
        UUID questId = UUID.randomUUID();

        QuestAnalytics analytics = new QuestAnalytics();
        analytics.setUserId(userId);
        analytics.setQuestId(questId);
        analytics.setTotalExperienceEarned(100L);
        analytics.setTotalQuestsCompleted(1);
        analytics.setCurrentLevel(1);
        analytics.setLastUpdated(LocalDateTime.now());

        QuestAnalytics saved = questAnalyticsRepository.save(analytics);

        assertNotNull(saved.getId());

        Optional<QuestAnalytics> found = questAnalyticsRepository.findByUserId(userId);
        assertTrue(found.isPresent());
        assertEquals(100L, found.get().getTotalExperienceEarned());
    }
}


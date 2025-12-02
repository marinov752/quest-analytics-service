package com.questanalytics.service;

import com.questanalytics.domain.entity.QuestAnalytics;
import com.questanalytics.repository.QuestAnalyticsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestAnalyticsServiceTest {

    @Mock
    private QuestAnalyticsRepository questAnalyticsRepository;

    @InjectMocks
    private QuestAnalyticsService questAnalyticsService;

    private UUID userId;
    private UUID questId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        questId = UUID.randomUUID();
    }

    @Test
    void testRecordQuestCompletion_NewAnalytics() {
        when(questAnalyticsRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(questAnalyticsRepository.save(any(QuestAnalytics.class))).thenAnswer(invocation -> {
            QuestAnalytics analytics = invocation.getArgument(0);
            analytics.setId(UUID.randomUUID());
            return analytics;
        });

        questAnalyticsService.recordQuestCompletion(userId, questId, 100L);

        verify(questAnalyticsRepository, times(1)).save(any(QuestAnalytics.class));
    }

    @Test
    void testRecordQuestCompletion_ExistingAnalytics() {
        QuestAnalytics existing = new QuestAnalytics();
        existing.setId(UUID.randomUUID());
        existing.setUserId(userId);
        existing.setTotalExperienceEarned(50L);
        existing.setTotalQuestsCompleted(1);

        when(questAnalyticsRepository.findByUserId(userId)).thenReturn(Optional.of(existing));
        when(questAnalyticsRepository.save(any(QuestAnalytics.class))).thenReturn(existing);

        questAnalyticsService.recordQuestCompletion(userId, questId, 100L);

        verify(questAnalyticsRepository, times(1)).save(any(QuestAnalytics.class));
    }
}


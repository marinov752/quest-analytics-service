package com.questanalytics.controller;

import com.questanalytics.service.QuestAnalyticsService;
import com.questanalytics.service.UserStatsUpdateDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/analytics")
public class QuestAnalyticsController {

    private static final Logger logger = LoggerFactory.getLogger(QuestAnalyticsController.class);
    private final QuestAnalyticsService questAnalyticsService;

    public QuestAnalyticsController(QuestAnalyticsService questAnalyticsService) {
        this.questAnalyticsService = questAnalyticsService;
    }

    @PostMapping("/quest-completion")
    public ResponseEntity<Void> recordQuestCompletion(
            @RequestParam UUID userId,
            @RequestParam UUID questId,
            @RequestParam Long experiencePoints) {
        logger.info("POST /api/analytics/quest-completion called for user {}", userId);
        questAnalyticsService.recordQuestCompletion(userId, questId, experiencePoints);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/user-stats")
    public ResponseEntity<Void> updateUserStatistics(@RequestBody UserStatsUpdateDto statsDto) {
        logger.info("PUT /api/analytics/user-stats called for user {}", statsDto.getUserId());
        questAnalyticsService.updateUserStatistics(
            statsDto.getUserId(),
            statsDto.getTotalExperience(),
            statsDto.getLevel(),
            statsDto.getQuestsCompleted()
        );
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> deleteAnalyticsData(@PathVariable UUID userId) {
        logger.info("DELETE /api/analytics/user/{} called", userId);
        questAnalyticsService.deleteAnalyticsData(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getAnalyticsData(@PathVariable UUID userId) {
        logger.info("GET /api/analytics/user/{} called", userId);
        Map<String, Object> data = questAnalyticsService.getAnalyticsData(userId);
        return ResponseEntity.ok(data);
    }
}


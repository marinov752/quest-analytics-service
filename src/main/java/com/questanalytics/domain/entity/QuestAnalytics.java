package com.questanalytics.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "quest_analytics")
public class QuestAnalytics {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Column(nullable = false)
    private UUID userId;

    @NotNull
    @Column(nullable = false)
    private UUID questId;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false)
    private Long totalExperienceEarned = 0L;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false)
    private Integer totalQuestsCompleted = 0;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false)
    private Integer currentLevel = 1;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime lastUpdated;

    public QuestAnalytics() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getQuestId() {
        return questId;
    }

    public void setQuestId(UUID questId) {
        this.questId = questId;
    }

    public Long getTotalExperienceEarned() {
        return totalExperienceEarned;
    }

    public void setTotalExperienceEarned(Long totalExperienceEarned) {
        this.totalExperienceEarned = totalExperienceEarned;
    }

    public Integer getTotalQuestsCompleted() {
        return totalQuestsCompleted;
    }

    public void setTotalQuestsCompleted(Integer totalQuestsCompleted) {
        this.totalQuestsCompleted = totalQuestsCompleted;
    }

    public Integer getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(Integer currentLevel) {
        this.currentLevel = currentLevel;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}


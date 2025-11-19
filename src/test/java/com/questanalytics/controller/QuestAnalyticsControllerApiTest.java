package com.questanalytics.controller;

import com.questanalytics.service.QuestAnalyticsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QuestAnalyticsController.class)
class QuestAnalyticsControllerApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuestAnalyticsService questAnalyticsService;

    @Test
    void testRecordQuestCompletion() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID questId = UUID.randomUUID();

        doNothing().when(questAnalyticsService).recordQuestCompletion(any(), any(), anyLong());

        mockMvc.perform(post("/api/analytics/quest-completion")
                .param("userId", userId.toString())
                .param("questId", questId.toString())
                .param("experiencePoints", "100"))
            .andExpect(status().isCreated());

        verify(questAnalyticsService, times(1)).recordQuestCompletion(userId, questId, 100L);
    }

    @Test
    void testGetAnalyticsData() throws Exception {
        UUID userId = UUID.randomUUID();
        Map<String, Object> data = new HashMap<>();
        data.put("totalExperienceEarned", 100L);
        data.put("totalQuestsCompleted", 1);

        when(questAnalyticsService.getAnalyticsData(userId)).thenReturn(data);

        mockMvc.perform(get("/api/analytics/user/" + userId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalExperienceEarned").value(100L))
            .andExpect(jsonPath("$.totalQuestsCompleted").value(1));
    }
}


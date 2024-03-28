package com.fashion.digital.politicalspeech.controller;

import com.fashion.digital.politicalspeech.model.EvaluationResult;
import com.fashion.digital.politicalspeech.service.SpeechService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class SpeechControllerIntegrationTest {

    @MockBean
    SpeechService speechService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void test_evaluate_when_url_is_correct() throws Exception {
        // Input
        Map<String, String> urls = new HashMap<>();
        urls.put("url1", "http://example.com/speech1.csv");
        urls.put("url2", "http://example.com/speech2.csv");
        final EvaluationResult expectedResponse = getEvaluationResult();

        // When
        when(speechService.evaluate(urls.values().stream().toList())).thenReturn(expectedResponse);

        // Perform HTTP request and verify response
        mockMvc.perform(MockMvcRequestBuilders.get("/evaluation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("url1", urls.get("url1"))
                        .param("url2", urls.get("url2")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\"mostSpeeches\":\"most speech\",\"mostSecurity\":\"most security\",\"leastWordy\":\"least Words\"}"));

        verify(speechService, times(1)).evaluate(urls.values().stream().toList());
    }

    @Test
    void test_evaluate_when_url_is_null() throws Exception {
        // Perform HTTP request and verify response
        mockMvc.perform(MockMvcRequestBuilders.get("/evaluation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("url1", "url"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        verifyNoInteractions(speechService);
    }

    @Test
    void test_evaluate_when_url_is_not_correct() throws Exception {
        // Perform HTTP request and verify response
        mockMvc.perform(MockMvcRequestBuilders.get("/evaluation")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        verifyNoInteractions(speechService);
    }

    private EvaluationResult getEvaluationResult() {
        String mostSpeech = "most speech";
        String mostSecurity = "most security";
        String leastWords = "least Words";
        return new EvaluationResult(mostSpeech, mostSecurity, leastWords);
    }
}
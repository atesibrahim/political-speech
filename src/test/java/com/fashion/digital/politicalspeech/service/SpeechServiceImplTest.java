package com.fashion.digital.politicalspeech.service;

import com.fashion.digital.politicalspeech.model.EvaluationResult;
import com.fashion.digital.politicalspeech.model.Speech;
import com.fashion.digital.politicalspeech.repository.SpeechRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SpeechServiceImplTest {
    @Mock
    private SpeechRepository speechRepository;

    @InjectMocks
    private SpeechServiceImpl speechService;

    private List<Speech> speeches;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void test_evaluate_when_data_exists() {
        // Stub the speechRepository to return the sample speeches
        speeches = getSpeeches("homeland security", "2013-01-02");
        when(speechRepository.fetchSpeechesFromUrls(anyList())).thenReturn(speeches);

        // Perform evaluation
        EvaluationResult result = speechService.evaluate(Arrays.asList("url1", "url2"));

        // Verify the evaluation result
        assertEquals("Politician 1", result.getMostSpeeches());
        assertEquals("Politician 2", result.getMostSecurity());
        assertEquals("Politician 3", result.getLeastWordy());

        verify(speechRepository, times(1)).fetchSpeechesFromUrls(anyList());
    }

    @Test
    void test_evaluate_when_no_homeland_security() {
        // Stub the speechRepository to return the sample speeches
        speeches = getSpeeches("topic", "2013-01-02");
        when(speechRepository.fetchSpeechesFromUrls(anyList())).thenReturn(speeches);

        // Perform evaluation
        EvaluationResult result = speechService.evaluate(Arrays.asList("url1", "url2"));

        // Verify the evaluation result
        assertEquals("Politician 1", result.getMostSpeeches());
        assertNull(result.getMostSecurity());
        assertEquals("Politician 3", result.getLeastWordy());

        verify(speechRepository, times(1)).fetchSpeechesFromUrls(anyList());
    }

    @Test
    void test_evaluate_when_no_exist_date_2013() {
        // Stub the speechRepository to return the sample speeches
        speeches = getSpeeches("topic", "2011-01-02");
        when(speechRepository.fetchSpeechesFromUrls(anyList())).thenReturn(speeches);

        // Perform evaluation
        EvaluationResult result = speechService.evaluate(Arrays.asList("url1", "url2"));

        // Verify the evaluation result
        assertNull(result.getMostSpeeches());
        assertNull(result.getMostSecurity());
        assertEquals("Politician 3", result.getLeastWordy());

        verify(speechRepository, times(1)).fetchSpeechesFromUrls(anyList());
    }

    @Test
    void test_evaluate_when_year_is_not_parseable() {
        // Stub the speechRepository to return the sample speeches
        speeches = getSpeeches("topic", "year");
        when(speechRepository.fetchSpeechesFromUrls(anyList())).thenReturn(speeches);
        String expectedError = "There is an error during parsing yearFor input string: \"year\"";
        // Perform evaluation
        try {
            speechService.evaluate(Arrays.asList("url1", "url2"));
        } catch (RuntimeException re) {
            assertEquals(expectedError, re.getMessage());
        }

        verify(speechRepository, times(1)).fetchSpeechesFromUrls(anyList());
    }

    private List<Speech> getSpeeches(String topic, String date) {
        // Initialize sample speeches for testing
        Speech speech = Speech.builder().speaker("Politician 1").topic("Topic").date(date).words(150).build();
        Speech speech1 = Speech.builder().speaker("Politician 1").topic("Topic 1").date("2012-01-01").words(100).build();
        Speech speech2 = Speech.builder().speaker("Politician 2").topic(topic).date("2012-02-02").words(200).build();
        Speech speech3 = Speech.builder().speaker("Politician 3").topic("Topic 3").date("2012-03-03").words(50).build();

        return Arrays.asList(speech, speech1, speech2, speech3);
    }

}
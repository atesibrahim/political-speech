package com.fashion.digital.politicalspeech.repository;


import com.fashion.digital.politicalspeech.model.Speech;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.fashion.digital.politicalspeech.constant.Constants.DATE_FORMAT_ERROR;
import static com.fashion.digital.politicalspeech.constant.Constants.FILE_FORMAT_ERROR;
import static com.fashion.digital.politicalspeech.constant.Constants.FILE_READ_ERROR;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class SpeechRepositoryImplTest {
    @InjectMocks
    private SpeechRepositoryImpl speechRepository;

    @Test
    void test_fetch_speeches() {
        String url = "https://raw.githubusercontent.com/atesibrahim/csv-files/main/file2.csv";

        List<Speech> result = speechRepository.fetchSpeechesFromUrls(List.of(url));

        assertEquals(4, result.size());
    }

    @Test
    void test_fetch_speeches_when_file_is_corrupted() {
        String url = "https://raw.githubusercontent.com/atesibrahim/csv-files/main/corrupted_file.csv";
        try {
            speechRepository.fetchSpeechesFromUrls(List.of(url));
        } catch (RuntimeException re) {
            assertEquals(FILE_FORMAT_ERROR, re.getMessage());
        }
    }

    @Test
    void test_fetch_speeches_when_file_date_is_corrupted() {
        String url = "https://raw.githubusercontent.com/atesibrahim/csv-files/main/corrupted_file2.csv";

        try {
            speechRepository.fetchSpeechesFromUrls(List.of(url));
        } catch (RuntimeException re) {
            assertEquals(DATE_FORMAT_ERROR, re.getMessage());
        }
    }

    @Test
    void test_fetch_speeches_when_contents_not_exist() {
        String url = "https://raw.githubusercontent.com/atesibrahim/file2.csv";
        String expectedError = FILE_READ_ERROR + "Server returned HTTP response code: 400 for URL: https://raw.githubusercontent.com/atesibrahim/file2.csv";

        try{
            speechRepository.fetchSpeechesFromUrls(List.of(url));
        } catch (RuntimeException re) {
            assertEquals(expectedError,  re.getMessage());
        }
    }
}
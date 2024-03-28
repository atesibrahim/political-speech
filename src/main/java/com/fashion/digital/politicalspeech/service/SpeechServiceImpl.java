package com.fashion.digital.politicalspeech.service;

import com.fashion.digital.politicalspeech.model.EvaluationResult;
import com.fashion.digital.politicalspeech.model.Speech;
import com.fashion.digital.politicalspeech.repository.SpeechRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static com.fashion.digital.politicalspeech.constant.Constants.PARSING_YEAR_ERROR;

@Service
@RequiredArgsConstructor
public class SpeechServiceImpl implements SpeechService {

    public static final int YEAR = 2013;
    public static final String HOMELAND_SECURITY = "homeland security";
    private final SpeechRepository speechRepository;

    @Override
    public EvaluationResult evaluate(List<String> urls) {
        List<Speech> speeches = speechRepository.fetchSpeechesFromUrls(urls);

        String mostSpeeches = mostSpeeches(speeches);
        String mostSecurity = mostSecurity(speeches);
        String leastWordy = leastWordy(speeches);

        return new EvaluationResult(mostSpeeches, mostSecurity, leastWordy);
    }


    private String mostSpeeches(List<Speech> speeches) {
        try {
            Map<String, Long> speechesCountByPolitician = speeches.stream()
                    .filter(speech -> Integer.parseInt(speech.getDate().split("-")[0]) == YEAR)
                    .collect(Collectors.groupingBy(Speech::getSpeaker, Collectors.counting()));

            if (speechesCountByPolitician.isEmpty()) {
                return null;
            }

            return Collections.max(speechesCountByPolitician.entrySet(), Map.Entry.comparingByValue()).getKey();
        } catch (NumberFormatException | NullPointerException exception) {
            throw new RuntimeException(PARSING_YEAR_ERROR + exception.getMessage());
        }
    }

    private String mostSecurity(List<Speech> speeches) {
        Map<String, Long> securitySpeechesCountByPolitician = speeches.stream()
                .filter(speech -> speech.getTopic().equalsIgnoreCase(HOMELAND_SECURITY))
                .collect(Collectors.groupingBy(Speech::getSpeaker, Collectors.counting()));

        if (securitySpeechesCountByPolitician.isEmpty()) {
            return null;
        }

        return Collections.max(securitySpeechesCountByPolitician.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    private String leastWordy(List<Speech> speeches) {
        final Map<String, Integer> totalWordsBySpeaker = new TreeMap<>();;

        for (Speech speech : speeches) {
            final String speaker = speech.getSpeaker();
            int words = speech.getWords();
            totalWordsBySpeaker.put(speaker, totalWordsBySpeaker.getOrDefault(speaker, 0) + words);
        }

        return Collections.min(totalWordsBySpeaker.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
    }
}

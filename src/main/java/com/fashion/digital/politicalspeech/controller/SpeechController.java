package com.fashion.digital.politicalspeech.controller;

import com.fashion.digital.politicalspeech.model.EvaluationResult;
import com.fashion.digital.politicalspeech.service.SpeechService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Map;

import static com.fashion.digital.politicalspeech.constant.Constants.INVALID_URL_FORMAT;
import static com.fashion.digital.politicalspeech.constant.Constants.URL_NOT_FOUND;

@RestController
@RequiredArgsConstructor
public class SpeechController {

    private final SpeechService speechService;

    @GetMapping("/evaluation")
    public EvaluationResult evaluate(@RequestParam  Map<String, String> urls) {
        checkUrls(urls);
        return speechService.evaluate(urls.values().stream().toList());
    }

    private void checkUrls(Map<String, String> urls) {
        if(urls.isEmpty()) {
            throw new RuntimeException(URL_NOT_FOUND);
        }

        if (!validateURLs(urls.values())) {
            throw new RuntimeException(INVALID_URL_FORMAT);
        }
    }

    private boolean validateURLs(Collection<String> urls) {
        for (String url : urls) {
            try {
                new URL(url);
            } catch (MalformedURLException e) {
                return false;
            }
        }
        return true;
    }
}

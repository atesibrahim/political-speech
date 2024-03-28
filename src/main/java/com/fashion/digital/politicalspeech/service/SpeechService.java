package com.fashion.digital.politicalspeech.service;

import com.fashion.digital.politicalspeech.model.EvaluationResult;

import java.util.List;

public interface SpeechService {
    EvaluationResult evaluate(List<String> urls);
}

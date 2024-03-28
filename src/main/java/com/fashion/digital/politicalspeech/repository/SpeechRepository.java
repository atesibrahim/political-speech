package com.fashion.digital.politicalspeech.repository;

import com.fashion.digital.politicalspeech.model.Speech;
import java.util.List;

public interface SpeechRepository {
    List<Speech> getSpeeches(List<String> urls);
}

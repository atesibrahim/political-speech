package com.fashion.digital.politicalspeech.repository;

import com.fashion.digital.politicalspeech.model.Speech;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.fashion.digital.politicalspeech.constant.Constants.DATE_FORMAT_ERROR;
import static com.fashion.digital.politicalspeech.constant.Constants.FILE_FORMAT_ERROR;
import static com.fashion.digital.politicalspeech.constant.Constants.FILE_READ_ERROR;

@Repository
@RequiredArgsConstructor
public class SpeechRepositoryImpl implements SpeechRepository {
    private static final char BOM_CHAR = '\uFEFF';
    private static final String DATE_REGEX = "\\d{4}-\\d{2}-\\d{2}";
    final List<Speech> speeches = new ArrayList<>();

    @Override
    public List<Speech> getSpeeches(List<String> urls) {
        for (String url : urls) saveSpeeches(url);
        return speeches;
    }

    private void saveSpeeches(String url) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(url).openStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length == 4) {
                    Speech speech = Speech.builder().build();
                    speech.setSpeaker(getSpeaker(data[0].trim()));
                    speech.setTopic(data[1].trim());
                    speech.setDate(getDate(data[2].trim()));
                    speech.setWords(Integer.parseInt(data[3].trim()));
                    speeches.add(speech);
                } else {
                    throw new RuntimeException(FILE_FORMAT_ERROR);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(FILE_READ_ERROR + e.getMessage());
        }
    }

    private String getSpeaker(String data) {
        return data.charAt(0) == BOM_CHAR ? data.substring(1) : data;
    }

    private String getDate(String date) {
        if (isValidDate(date)) {
            return date;
        }
        throw new RuntimeException(DATE_FORMAT_ERROR);
    }

    private boolean isValidDate(String dateStr) {
        Pattern pattern = Pattern.compile(DATE_REGEX);
        Matcher matcher = pattern.matcher(dateStr);
        return matcher.matches();
    }
}



package com.fashion.digital.politicalspeech.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Speech {
    private String speaker;
    private String topic;
    private String date;
    private int words;
}

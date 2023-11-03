package com.novare.wordcloud.payload;

import java.util.Map;

public class WordCloudData {
    private Map<String, Integer> wordFrequencies;

    public WordCloudData(Map<String, Integer> wordFrequencies) {
        this.wordFrequencies = wordFrequencies;
    }

    public Map<String, Integer> getWordFrequencies() {
        return wordFrequencies;
    }

    public void setWordFrequencies(Map<String, Integer> wordFrequencies) {
        this.wordFrequencies = wordFrequencies;
    }
}
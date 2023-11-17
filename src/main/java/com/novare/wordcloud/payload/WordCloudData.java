package com.novare.wordcloud.payload;

import java.util.Map;

public class WordCloudData {
    private Map<String, Integer> wordFrequencies;

    public WordCloudData(Map<String, Integer> wordFrequencies) {
        this.wordFrequencies = wordFrequencies;
    }

    // unused content -3
    public Map<String, Integer> getWordFrequencies() {
        return wordFrequencies;
    }

    // unused content -3
    public void setWordFrequencies(Map<String, Integer> wordFrequencies) {
        this.wordFrequencies = wordFrequencies;
    }
}
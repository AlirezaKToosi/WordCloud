package com.novare.wordcloud.payload;

public class WordCloudRequest {
    private String rssFeedUrl;
    private int wordFrequencyThreshold;

    public String getRssFeedUrl() {
        return rssFeedUrl;
    }

    public int getWordFrequencyThreshold() {
        return wordFrequencyThreshold;
    }

    public void setRssFeedUrl(String rssFeedUrl) {
        this.rssFeedUrl = rssFeedUrl;
    }

    public void setWordFrequencyThreshold(int wordFrequencyThreshold) {
        this.wordFrequencyThreshold = wordFrequencyThreshold;
    }
}
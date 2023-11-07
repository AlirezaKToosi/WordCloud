package com.novare.wordcloud.service;

import com.novare.wordcloud.payload.WordCloudData;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WordCloudService {

    public static WordCloudData generateWordCloud(String rssFeedUrl, int wordFrequencyThreshold) {
        try {
            URL feedUrl = new URL(rssFeedUrl);
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedUrl));

            List<String> allWords = new ArrayList<>();
            List<SyndEntry> entries = feed.getEntries();
            for (SyndEntry entry : entries) {
                if (entry.getTitleEx() != null) {
                    String entryText = entry.getTitleEx().getValue();
                    List<String> words = processEntryText(entryText);
                    allWords.addAll(words);
                }
            }

            // Remove stop words
            List<String> filteredWords = filterStopWords(allWords);

            // Word frequencies
            Map<String, Integer> wordFrequencies = countWordFrequencies(filteredWords);

            // Threshold
            Map<String, Integer> filteredWordsByThreshold = filterWordsByThreshold(wordFrequencies, wordFrequencyThreshold);

            return new WordCloudData(filteredWordsByThreshold);
        } catch (IOException | FeedException e) {
            throw new RuntimeException("Error fetching or parsing the RSS feed.", e);
        }
    }

    private static List<String> processEntryText(String text) {
        // Split the text by whitespace
        return Arrays.asList(text.split("\\s+"));
    }

    private static List<String> filterStopWords(List<String> words) {
        List<String> stopWords = List.of("and", "or", "the", "a", "in", "for", "is", "to", "on");
        return words.stream()
                .filter(word -> !stopWords.contains(word.toLowerCase()))
                .collect(Collectors.toList());
    }

    private static Map<String, Integer> countWordFrequencies(List<String> words) {
        Map<String, Integer> wordCount = new HashMap<>();
        for (String word : words) {
            wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
        }
        return wordCount;
    }

    private static Map<String, Integer> filterWordsByThreshold(Map<String, Integer> wordFrequencies, int threshold) {
        Map<String, Integer> filteredWords = new HashMap<>();
        for (Map.Entry<String, Integer> entry : wordFrequencies.entrySet()) {
            if (entry.getValue() >= threshold) {
                filteredWords.put(entry.getKey(), entry.getValue());
            }
        }
        return filteredWords;
    }
}

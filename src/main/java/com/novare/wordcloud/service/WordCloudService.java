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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WordCloudService {

    public static WordCloudData generateWordCloud(String rssFeedUrl, int wordFrequencyThreshold) {
        try {
            URL feedUrl = new URL(rssFeedUrl);
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedUrl));

            // Extract and process feed entries
            List<String> allWords = new ArrayList<>();
            List<SyndEntry> entries = feed.getEntries();
            for (SyndEntry entry : entries) {
                if (entry.getDescription() != null) {
                    String entryText = entry.getDescription().getValue();
                    List<String> words = processEntryText(entryText);
                    allWords.addAll(words);
                }
            }

            // Count word frequencies
            Map<String, Integer> wordFrequencies = countWordFrequencies(allWords);

            // Filter words based on the threshold
            Map<String, Integer> filteredWords = filterWordsByThreshold(wordFrequencies, wordFrequencyThreshold);

            return new WordCloudData(filteredWords);
        } catch (IOException | FeedException e) {
            // Handle exceptions appropriately
            throw new RuntimeException("Error fetching or parsing the RSS feed.", e);
        }
    }

    private static List<String> processEntryText(String text) {
        // Implement tokenization and text processing logic here
        // For simplicity, you can split the text by whitespace
        return List.of(text.split("\\s+"));
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

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

    // Why word frequency is moved to the backend? What happens if the user wants to dynamically change the number of shown words?
    public static WordCloudData generateWordCloud(String rssFeedUrl, int wordFrequencyThreshold) {
        try {
            URL feedUrl = new URL(rssFeedUrl);
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedUrl));
            // why this space between properties?
            List<String> allWords = new ArrayList<>();
            List<SyndEntry> entries = feed.getEntries();
            //
            // ad space between properties and actions
            for (SyndEntry entry : entries) {
                if (entry.getTitleEx() != null) {
                    String entryText = entry.getTitleEx().getValue();
                    List<String> words = processEntryText(entryText);
                    allWords.addAll(words);
                }
            }

            // Remove stop words (comments -1, look that you need to add more context via comment, meaning the naming can be improved)
            List<String> filteredWords = filterStopWords(allWords);

            // Word frequencies (comments -1 note that your comment and the property name are the same)
            Map<String, Integer> wordFrequencies = countWordFrequencies(filteredWords);

            // Threshold (same issue -1)
            Map<String, Integer> filteredWordsByThreshold = filterWordsByThreshold(wordFrequencies, wordFrequencyThreshold);

            return new WordCloudData(filteredWordsByThreshold);
        } catch (IOException | FeedException e) {
            // naming -1 don't use "e" use "error"
            throw new RuntimeException("Error fetching or parsing the RSS feed.", e);
        }
    }

    private static List<String> processEntryText(String text) {
        // Split the text by whitespace
        return Arrays.asList(text.split("\\s+"));
    }

    private static List<String> filterStopWords(List<String> words) {
        List<String> stopWords = List.of("and", "or", "the", "a", "in", "for", "is", "to", "on");
        //
        // space between properties and actions
        return words.stream()
                .filter(word -> !stopWords.contains(word.toLowerCase()))
                .collect(Collectors.toList());
    }

    private static Map<String, Integer> countWordFrequencies(List<String> words) {
        Map<String, Integer> wordCount = new HashMap<>();
        //
        // space between properties and actions
        for (String word : words) {
            wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
        }
        //
        // space between actions and return
        return wordCount;
    }

    private static Map<String, Integer> filterWordsByThreshold(Map<String, Integer> wordFrequencies, int threshold) {
        Map<String, Integer> filteredWords = new HashMap<>();
        //
        for (Map.Entry<String, Integer> entry : wordFrequencies.entrySet()) {
            if (entry.getValue() >= threshold) {
                filteredWords.put(entry.getKey(), entry.getValue());
            }
        }
        //
        return filteredWords;
    }
}

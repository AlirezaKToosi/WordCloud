package com.novare.wordcloud.controller;


import com.novare.wordcloud.payload.WordCloudData;
import com.novare.wordcloud.payload.WordCloudRequest;
import com.novare.wordcloud.service.WordCloudService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class WordCloudController {
    @PostMapping("/api/wordcloud/generate")
    public ResponseEntity<WordCloudData> generateWordCloud(@RequestBody WordCloudRequest request) {
        String rssFeedUrl = request.getRssFeedUrl();
        int wordFrequencyThreshold = request.getWordFrequencyThreshold();

        // Fetch and parse the RSS feed, process data, and generate the word cloud
        WordCloudData wordCloudData = WordCloudService.generateWordCloud(rssFeedUrl, wordFrequencyThreshold);

        return ResponseEntity.ok(wordCloudData);
    }
}

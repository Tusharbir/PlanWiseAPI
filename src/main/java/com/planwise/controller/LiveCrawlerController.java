package com.planwise.controller;

import com.planwise.service.LiveCrawlerService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/crawler")
@CrossOrigin(origins = "*")
public class LiveCrawlerController {

    private final LiveCrawlerService liveCrawlerService;

    public LiveCrawlerController(LiveCrawlerService liveCrawlerService) {
        this.liveCrawlerService = liveCrawlerService;
    }

    /**
     * Crawl multiple pages starting from URL, extract emails, phones, urls.
     * @param url starting url to crawl (required)
     * @param maxPages max pages to crawl (optional, default 20)
     * @return aggregated extracted data
     */
    @GetMapping("/extract")
    public CompletableFuture<Map<String, Set<String>>> crawl(@RequestParam String url,
                                                             @RequestParam(required = false, defaultValue = "40") int maxPages) {
        return liveCrawlerService.crawlMultiPages(url, maxPages);
    }
}
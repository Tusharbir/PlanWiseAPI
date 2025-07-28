package com.planwise.crawler;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CrawlerController {

    @Autowired
    private CrawlerService crawlerService;

    @GetMapping("/crawl")
    public CrawlResult crawlWebsite(@RequestParam String url) {
        try {
            return crawlerService.crawlWebsite(url);
        } catch (Exception e) {
            CrawlResult errorResult = new CrawlResult();
            errorResult.setError("Failed to crawl website: " + e.getMessage());
            return errorResult;
        }
    }
}
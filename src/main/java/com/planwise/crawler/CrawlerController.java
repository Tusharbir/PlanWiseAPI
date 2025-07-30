package com.planwise.crawler;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins="*")
public class CrawlerController {

    @Autowired
    private CrawlerService crawlerService;

    @GetMapping("/crawl")
    public CrawlModel crawlWebsite(@RequestParam String url) {
        try {
            return crawlerService.crawlWebsite(url);
        } catch (Exception e) {
            CrawlModel errorResult = new CrawlModel();
            errorResult.setError("Failed to crawl website: " + e.getMessage());
            return errorResult;
        }
    }
}
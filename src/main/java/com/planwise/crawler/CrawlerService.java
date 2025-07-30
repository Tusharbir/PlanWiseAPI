package com.planwise.crawler;


import org.springframework.stereotype.Service;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Service
public class CrawlerService {

    // Phone number patterns for different regions
    private final Pattern phonePattern = Pattern.compile(
            "^\\+(1|91|44|61)[ -]?(\\(\\d{3}\\)|\\d{3})[ -]?\\d{3}[ -]?\\d{4}$"
    );

    // Email pattern for various domains
    private final Pattern emailPattern = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");



    public CrawlResult crawlWebsite(String url) throws Exception {
        CrawlResult result = new CrawlResult();
        result.setUrl(url);

        // Add http if not present
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }

        // Connect and get the webpage
        Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                .timeout(10000)
                .get();

        // Get all text content
        String pageText = doc.text();

        // Extract phone numbers
        result.setPhoneNumbers(extractMatches(pageText, phonePattern));

        // Extract emails
        result.setEmails(extractMatches(pageText, emailPattern));

        // Extract links
        result.setLinks(extractLinks(doc));

        return result;
    }

    private List<String> extractMatches(String text, Pattern pattern) {
        Set<String> matches = new HashSet<>(); // Use Set to avoid duplicates
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            String match = matcher.group().trim();
            if (!match.isEmpty()) {
                matches.add(match);
            }
        }

        return new ArrayList<>(matches);
    }

    private List<String> extractLinks(Document doc) {
        Set<String> links = new HashSet<>();
        Elements linkElements = doc.select("a[href]");

        for (Element link : linkElements) {
            String href = link.attr("abs:href"); // Get absolute URL
            if (!href.isEmpty() && (href.startsWith("http://") || href.startsWith("https://"))) {
                links.add(href);
            }
        }

        return new ArrayList<>(links);
    }
}
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
            "(?:\\+?1[-\\s]?)?" +                           // Optional +1 or 1 with separator
                    "(?:\\([2-9]\\d{2}\\)|[2-9]\\d{2})[-\\s]?" +   // Area code (parentheses optional)
                    "[2-9]\\d{2}[-\\s]?" +                         // Exchange code
                    "\\d{4}" +                                     // Number
                    "|" +                                          // OR
                    "(?:\\+?44[-\\s]?)?" +                         // UK: +44
                    "(?:\\(?0?[1-9]\\d{1,4}\\)?[-\\s]?)" +         // UK area code
                    "\\d{6,8}" +                                   // UK number
                    "|" +                                          // OR
                    "(?:\\+?91[-\\s]?)?" +                         // India: +91
                    "[6-9]\\d{9}" +                                // India mobile
                    "|" +                                          // OR
                    "(?:\\+?61[-\\s]?)?" +                         // Australia: +61
                    "[2-478]\\d{8}" +                              // Australia number
                    "|" +                                          // OR
                    "(?:\\+?49[-\\s]?)?" +                         // Germany: +49
                    "\\(?0?[1-9]\\d{1,4}\\)?[-\\s]?" +             // Germany area code
                    "\\d{6,8}" +                                   // Germany number
                    "|" +                                          // OR
                    "(?:\\+?33[-\\s]?)?" +                         // France: +33
                    "\\(?0?[1-9]\\d{8}\\)?" +                      // France number
                    "|" +                                          // OR
                    "(?:\\+?86[-\\s]?)?" +                         // China: +86
                    "1[3-9]\\d{9}" +                               // China mobile
                    "|" +                                          // OR
                    "(?:\\+?81[-\\s]?)?" +                         // Japan: +81
                    "\\(?0?[1-9]\\d{1,4}\\)?[-\\s]?" +             // Japan area code
                    "\\d{6,8}" +                                   // Japan number
                    "|" +                                          // OR
                    "(?:\\+?7[-\\s]?)?" +                          // Russia: +7
                    "[489]\\d{9}" +                                // Russia mobile
                    "|" +                                          // OR
                    "(?:\\+?52[-\\s]?)?" +                         // Mexico: +52
                    "1?[2-9]\\d{9}" +                              // Mexico number
                    "|" +                                          // OR
                    "(?:\\+?55[-\\s]?)?" +                         // Brazil: +55
                    "\\(?[1-9]{2}\\)?[-\\s]?" +                    // Brazil area code
                    "[9]?\\d{8}" +                                 // Brazil number
                    "|" +                                          // OR
                    "(?:\\+?27[-\\s]?)?" +                         // South Africa: +27
                    "[1-9]\\d{8}" +                                // South Africa number
                    "|" +                                          // OR
                    "(?:\\+?\\d{1,4}[-\\s]?)?" +                   // Generic international
                    "\\(?\\d{1,4}\\)?[-\\s]?" +                    // Generic area code
                    "\\d{6,10}",                                   // Generic number
            Pattern.CASE_INSENSITIVE
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
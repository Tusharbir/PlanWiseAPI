package com.planwise.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;
import java.util.concurrent.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@EnableAsync
public class LiveCrawlerService {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "\\b[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\\b"
    );

    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "(\\+1[ -]?)?(\\(?\\d{3}\\)?[ -]?)\\d{3}[ -]?\\d{4}"
    );

    private static final Pattern URL_PATTERN = Pattern.compile(
            "\\b(?:https?://|www\\.)" +
            "(?:[\\w\\-]+(?::[^@\\s]+)?@)?" +
            "(?:localhost|\\d{1,3}(?:\\.\\d{1,3}){3}|[\\w\\-]+(?:\\.[\\w\\-]+)*\\.[a-zA-Z]{2,})" +
            "(?::\\d{2,5})?(?:/[^\\s]*)?",
            Pattern.CASE_INSENSITIVE
    );

    private final ConcurrentMap<String, Map<String, Set<String>>> cache = new ConcurrentHashMap<>();

    @Async
    public CompletableFuture<Map<String, Set<String>>> crawlMultiPages(String startUrl, int maxPages) {
        Set<String> visited = ConcurrentHashMap.newKeySet();
        Queue<String> queue = new ConcurrentLinkedQueue<>();

        Set<String> allEmails = ConcurrentHashMap.newKeySet();
        Set<String> allPhones = ConcurrentHashMap.newKeySet();
        Set<String> allUrls = ConcurrentHashMap.newKeySet();

        queue.add(startUrl);

        while (!queue.isEmpty() && visited.size() < maxPages) {
            String currentUrl = queue.poll();

            if (currentUrl == null || visited.contains(currentUrl)) continue;

            visited.add(currentUrl);

            // Check cache
            if (cache.containsKey(currentUrl)) {
                Map<String, Set<String>> cachedData = cache.get(currentUrl);
                allEmails.addAll(cachedData.getOrDefault("emails", Collections.emptySet()));
                allPhones.addAll(cachedData.getOrDefault("phones", Collections.emptySet()));
                allUrls.addAll(cachedData.getOrDefault("urls", Collections.emptySet()));
                continue;
            }

            try {
                Document doc = Jsoup.connect(currentUrl).timeout(10000).get();
                String text = doc.text();

                // Extract data from current page
                Set<String> emails = extractMatches(EMAIL_PATTERN, text);
                Set<String> phones = extractMatches(PHONE_PATTERN, text);
                Set<String> urls = extractMatches(URL_PATTERN, text);

                allEmails.addAll(emails);
                allPhones.addAll(phones);
                allUrls.addAll(urls);

                // Cache the results
                Map<String, Set<String>> pageData = new HashMap<>();
                pageData.put("emails", emails);
                pageData.put("phones", phones);
                pageData.put("urls", urls);
                cache.put(currentUrl, pageData);

                // Find new URLs on this page to crawl next (only HTTP/HTTPS, avoid mailto, etc.)
                Elements links = doc.select("a[href]");
                for (Element link : links) {
                    String linkHref = link.absUrl("href");
                    if (linkHref.startsWith("http") && !visited.contains(linkHref)) {
                        queue.add(linkHref);
                    }
                }

            } catch (MalformedURLException e) {
                System.err.println("Invalid URL: " + currentUrl + " - " + e.getMessage());
            } catch (IOException e) {
                System.err.println("Failed to fetch URL: " + currentUrl + " - " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Error processing URL: " + currentUrl + " - " + e.getMessage());
            }
        }

        Map<String, Set<String>> aggregatedResult = new HashMap<>();
        aggregatedResult.put("emails", allEmails);
        aggregatedResult.put("phones", allPhones);
        aggregatedResult.put("urls", allUrls);

        return CompletableFuture.completedFuture(aggregatedResult);
    }

    private Set<String> extractMatches(Pattern pattern, String text) {
        Set<String> results = new HashSet<>();
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            results.add(matcher.group());
        }
        return results;
    }
}
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

    // Improved phone number patterns with proper length validation
    private static final Pattern phonePattern = Pattern.compile(
            // US/Canada: +1-XXX-XXX-XXXX, (XXX) XXX-XXXX, XXX-XXX-XXXX, etc.
            "(?:\\+?1[\\s\\-\\.]?)?(?:\\([2-9]\\d{2}\\)|[2-9]\\d{2})[\\s\\-\\.]?[2-9]\\d{2}[\\s\\-\\.]?\\d{4}\\b|" +

                    // India: +91-XXXXX-XXXXX, +91 XXXXX XXXXX, etc. (10 digits after country code)
                    "(?:\\+?91[\\s\\-\\.]?)?[6-9]\\d{9}\\b|" +

                    // UK: +44-XXXX-XXXXXX, +44 XXXX XXXXXX, etc.
                    "(?:\\+?44[\\s\\-\\.]?)?(?:0?[1-9]\\d{2,4}[\\s\\-\\.]?\\d{6,7}|0?[1-9]\\d{8,9})\\b|" +

                    // Australia: +61-X-XXXX-XXXX, etc.
                    "(?:\\+?61[\\s\\-\\.]?)?(?:0?[2-8]\\d{8}|0?4\\d{8})\\b|" +

                    // International format: +XX-XXX-XXX-XXXX (7-15 digits total)
                    "\\+\\d{1,3}[\\s\\-\\.]?\\d{3}[\\s\\-\\.]?\\d{3,4}[\\s\\-\\.]?\\d{4,6}\\b"
    );

    // Email pattern for various domains
    private final Pattern emailPattern = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");

    public CrawlModel crawlWebsite(String url) throws Exception {
        CrawlModel result = new CrawlModel();
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

        // Extract phone numbers with additional validation
        result.setPhoneNumbers(extractValidPhoneNumbers(pageText));

        // Extract emails
        result.setEmails(extractMatches(pageText, emailPattern));

        // Extract links
        result.setLinks(extractLinks(doc));

        return result;
    }

    private List<String> extractValidPhoneNumbers(String text) {
        Set<String> validPhones = new HashSet<>();
        Matcher matcher = phonePattern.matcher(text);

        while (matcher.find()) {
            String match = matcher.group().trim();
            if (!match.isEmpty() && isValidPhoneNumber(match)) {
                validPhones.add(match);
            }
        }

        return new ArrayList<>(validPhones);
    }

    private boolean isValidPhoneNumber(String phone) {
        // Remove all non-digit characters to count actual digits
        String digitsOnly = phone.replaceAll("[^0-9]", "");

        // Valid phone numbers should have between 7-15 digits (international standard)
        int digitCount = digitsOnly.length();

        // Additional validation rules
        if (digitCount < 7 || digitCount > 15) {
            return false;
        }

        // Exclude common non-phone patterns
        // Reject if it's all the same digit (like 1111111111)
        if (digitsOnly.matches("(\\d)\\1+")) {
            return false;
        }

        // Reject obvious patterns like 1234567890 or 0123456789
        if (digitsOnly.matches("0?1234567890?") || digitsOnly.matches("9876543210?")) {
            return false;
        }

        // For US numbers, ensure area code doesn't start with 0 or 1
        if (digitCount == 10 && (digitsOnly.charAt(0) == '0' || digitsOnly.charAt(0) == '1')) {
            return false;
        }

        // For US numbers with country code, check the same for area code
        if (digitCount == 11 && digitsOnly.startsWith("1") &&
                (digitsOnly.charAt(1) == '0' || digitsOnly.charAt(1) == '1')) {
            return false;
        }

        return true;
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
package com.planwise.crawler;


import java.util.List;
import java.util.ArrayList;

public class CrawlModel {
    private String url;
    private List<String> phoneNumbers;
    private List<String> emails;
    private List<String> links;
    private String error;

    public CrawlModel() {
        this.phoneNumbers = new ArrayList<>();
        this.emails = new ArrayList<>();
        this.links = new ArrayList<>();
    }

    // Getters and Setters
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public List<String> getPhoneNumbers() { return phoneNumbers; }
    public void setPhoneNumbers(List<String> phoneNumbers) { this.phoneNumbers = phoneNumbers; }

    public List<String> getEmails() { return emails; }
    public void setEmails(List<String> emails) { this.emails = emails; }


    public List<String> getLinks() { return links; }
    public void setLinks(List<String> links) { this.links = links; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}
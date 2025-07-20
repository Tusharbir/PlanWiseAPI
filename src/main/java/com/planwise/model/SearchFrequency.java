package com.planwise.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class SearchFrequency {
    @Id
    private String keyword;
    private int count;

    // Getters/Setters/No-args constructor...
    public SearchFrequency() {}
    public SearchFrequency(String keyword, int count) {
        this.keyword = keyword;
        this.count = count;
    }
    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }
    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }
}
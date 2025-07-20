package com.planwise.model;

public class SearchFrequency {
    private String keyword;
    private int count;

    public SearchFrequency(String keyword, int count) {
        this.keyword = keyword;
        this.count = count;
    }

    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }
    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }
}
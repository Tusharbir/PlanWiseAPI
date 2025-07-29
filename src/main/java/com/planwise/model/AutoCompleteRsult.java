package com.planwise.model;

public class AutoCompleteRsult {
    private final String word;
    private final int wordFrequency;
    private final int searchFrequency;


    public AutoCompleteRsult(String word, int wordFrequency, int searchFrequency) {
        this.word = word;
        this.wordFrequency = wordFrequency;
        this.searchFrequency = searchFrequency;

    }

    public String getWord()            { return word; }
    public int    getWordFrequency()   { return wordFrequency; }
    public int getSearchFrequency() { return searchFrequency; }

}

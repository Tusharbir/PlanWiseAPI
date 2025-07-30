package com.planwise.utilities;

import com.planwise.autocomplete.Extractor;


import java.util.Map;

public class UtilFunc {

    private static String resource = "merged-csv.csv";

    public static Map<String, Integer> loadVocabulary() {
        return Extractor.extractVocabulary(resource);
    }
}

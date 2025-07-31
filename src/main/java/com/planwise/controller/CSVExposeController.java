package com.planwise.controller;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")

public class CSVExposeController {
    @GetMapping("/view-csv")
    public List<String> viewCsv() throws IOException {
        Path path = Paths.get("src/main/resources/merged-csv.csv");
        System.out.println("Reading merged-csv.csv from: " + path.toAbsolutePath());
        return Files.readAllLines(path);
    }

    @GetMapping("/view-frequency-csv")
    public List<String> viewSearchFrequencyCSV() throws IOException {
        Path path = Paths.get("data/search_frequency.csv");
        System.out.println("Reading search_frequency csv from: " + path.toAbsolutePath());
        return Files.readAllLines(path);
    }
}

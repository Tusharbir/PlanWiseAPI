package com.planwise.model;

public class Plan {

    private String site;
    private String provider;         // Leaving it blank for now
    private String planName;
    private String dataLimit;
    private String price;
    private String downloadSpeed;
    private String uploadSpeed;
    private String technology;
    private String features;
    private String description;
    private String pros;
    private String modemLinks;
    private String planURL;

    public Plan(String site, String provider, String planName, String dataLimit, String price, String downloadSpeed, String uploadSpeed, String technology, String features, String description, String pros, String modemLinks, String planURL) {
        this.site = site;
        this.provider = provider;
        this.planName = planName;
        this.dataLimit = dataLimit;
        this.price = price;
        this.downloadSpeed = downloadSpeed;
        this.uploadSpeed = uploadSpeed;
        this.technology = technology;
        this.features = features;
        this.description = description;
        this.pros = pros;
        this.modemLinks = modemLinks;
        this.planURL = planURL;
    }

    // Constructor (matches DataLoaderService)
//    public Plan(String site, String provider, String planName, String technology,
//                String downloadSpeed, String uploadSpeed, String price,
//                String dataLimit, String planURL, String features, String description, String pros, String modemLinks) {
//        this.site = site;
//        this.provider = provider;
//        this.planName = planName;
//        this.technology = technology;
//        this.downloadSpeed = downloadSpeed;
//        this.uploadSpeed = uploadSpeed;
//        this.price = price;
//        this.dataLimit = dataLimit;
//        this.planURL = planURL;
//        this.features = features;
//        this.description = description;
//        this.pros = pros;
//        this.modemLinks = modemLinks;
//    }

    // Getters (Spring Boot uses these for JSON serialization)
    public String getSite() {
        return site;
    }

    public String getProvider() {
        return provider;
    }

    public String getPlanName() {
        return planName;
    }

    public String getTechnology() {
        return technology;
    }

    public String getDownloadSpeed() {
        return downloadSpeed;
    }

    public String getUploadSpeed() {
        return uploadSpeed;
    }

    public String getPrice() {
        return price;
    }

    public String getDataLimit() {
        return dataLimit;
    }

    public String getPlanURL() {
        return planURL;
    }
    public String getFeatures() {return features;}

    public String getDescription() {return description;}

    public String getPros(){ return pros;}

    public String getModemLinks(){ return modemLinks;}
}


//public class Plan {
//    private String site;
//    private String planName;
//    private String downloadSpeed;
//    private String uploadSpeed;
//    private String technology;
//    private String price;
//    private String planURL;
//
//    // Constructor
//    public Plan(String site, String planName, String downloadSpeed, String uploadSpeed,
//                String technology, String price, String planURL) {
//        this.site = site;
//        this.planName = planName;
//        this.downloadSpeed = downloadSpeed;
//        this.uploadSpeed = uploadSpeed;
//        this.technology = technology;
//        this.price = price;
//        this.planURL = planURL;
//    }
//
//    // Getters (Spring uses these to generate JSON)
//    public String getSite() { return site; }
//    public String getPlanName() { return planName; }
//    public String getDownloadSpeed() { return downloadSpeed; }
//    public String getUploadSpeed() { return uploadSpeed; }
//    public String getTechnology() { return technology; }
//    public String getPrice() { return price; }
//    public String getPlanURL() { return planURL; }
//}
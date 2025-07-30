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

    public Plan(String site, String provider, String planName, String dataLimit, String price, String downloadSpeed, String uploadSpeed, String technology, String features) {
        this.site = site;
        this.provider = provider;
        this.planName = planName;
        this.dataLimit = dataLimit;
        this.price = price;
        this.downloadSpeed = downloadSpeed;
        this.uploadSpeed = uploadSpeed;
        this.technology = technology;
        this.features = features;

    }


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

    public String getFeatures() {
        return features;
    }

}
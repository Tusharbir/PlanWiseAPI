package com.planwise.model;

import jakarta.persistence.*;

@Entity
@Table(name = "plans") // Table name in your MySQL
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "company")
    private String company;

    @Column(name = "plan_name")
    private String planName;

    @Column(name = "data_limited")
    private String dataLimited;

    @Column(name = "price")
    private String price;

    @Column(name = "download_speed")
    private String downloadSpeed;

    @Column(name = "upload_speed")
    private String uploadSpeed;

    @Column(name = "technology")
    private String technology;

    @Column(name = "features")
    private String features;

    // --- Constructors ---
    public Plan() {} // Required by JPA

    public Plan(Integer id, String company, String planName, String dataLimited, String price,
                String downloadSpeed, String uploadSpeed, String technology, String features) {
        this.id = id;
        this.company = company;
        this.planName = planName;
        this.dataLimited = dataLimited;
        this.price = price;
        this.downloadSpeed = downloadSpeed;
        this.uploadSpeed = uploadSpeed;
        this.technology = technology;
        this.features = features;
    }

    // --- Getters ---
    public Integer getId() { return id; }
    public String getCompany() { return company; }
    public String getPlanName() { return planName; }
    public String getDataLimited() { return dataLimited; }
    public String getPrice() { return price; }
    public String getDownloadSpeed() { return downloadSpeed; }
    public String getUploadSpeed() { return uploadSpeed; }
    public String getTechnology() { return technology; }
    public String getFeatures() { return features; }

    // --- Setters (Optional, for JPA) ---
    public void setId(Integer id) { this.id = id; }
    public void setCompany(String company) { this.company = company; }
    public void setPlanName(String planName) { this.planName = planName; }
    public void setDataLimited(String dataLimited) { this.dataLimited = dataLimited; }
    public void setPrice(String price) { this.price = price; }
    public void setDownloadSpeed(String downloadSpeed) { this.downloadSpeed = downloadSpeed; }
    public void setUploadSpeed(String uploadSpeed) { this.uploadSpeed = uploadSpeed; }
    public void setTechnology(String technology) { this.technology = technology; }
    public void setFeatures(String features) { this.features = features; }
}
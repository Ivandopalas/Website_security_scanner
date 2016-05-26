package com.bsu.famcs.wscan.model;

import org.zaproxy.clientapi.core.Alert;

import java.util.List;

public class SiteData {
    private String siteLink;
    private String siteCategory;
    //TODO remove addiction
    private List<Alert> scanResults;

    private int lowRiskCount;
    private int mediumRiskCount;
    private int highRiskCount;



    public String getSiteLink() {
        return siteLink;
    }

    public void setSiteLink(String siteLink) {
        this.siteLink = siteLink;
    }

    public String getSiteCategory() {
        return siteCategory;
    }

    public void setSiteCategory(String siteCategory) {
        this.siteCategory = siteCategory;
    }

    public List<Alert> getScanResults() {
        return scanResults;
    }

    public void setScanResults(List<Alert> scanResults) {
        this.scanResults = scanResults;
    }

    public int getLowRiskCount() {
        return lowRiskCount;
    }

    public void setLowRiskCount(int lowRiskCount) {
        this.lowRiskCount = lowRiskCount;
    }

    public int getMediumRiskCount() {
        return mediumRiskCount;
    }

    public void setMediumRiskCount(int mediumRiskCount) {
        this.mediumRiskCount = mediumRiskCount;
    }

    public int getHighRiskCount() {
        return highRiskCount;
    }

    public void setHighRiskCount(int highRiskCount) {
        this.highRiskCount = highRiskCount;
    }
}


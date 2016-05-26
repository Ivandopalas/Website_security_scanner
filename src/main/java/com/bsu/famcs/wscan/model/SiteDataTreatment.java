package com.bsu.famcs.wscan.model;

import java.util.HashMap;
import java.util.Map;

public class SiteDataTreatment {

    private Map<String,SiteData> mapSiteData;
    private String reportingOutputGenName;

    public void init(){
        mapSiteData = new HashMap<>();
    }

    public Map<String, SiteData> getMapSiteData() {
        return mapSiteData;
    }

    public void setMapSiteData(Map<String, SiteData> mapSiteData) {
        this.mapSiteData = mapSiteData;
    }

    public String getReportingOutputGenName() {
        return reportingOutputGenName;
    }

    public void setReportingOutputGenName(String reportingOutputGenName) {
        this.reportingOutputGenName = reportingOutputGenName;
    }
}

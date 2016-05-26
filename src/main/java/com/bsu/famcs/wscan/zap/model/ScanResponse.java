package com.bsu.famcs.wscan.zap.model;

import org.zaproxy.clientapi.core.ApiResponse;
import org.zaproxy.clientapi.core.ApiResponseList;
import org.zaproxy.clientapi.core.ApiResponseSet;

import java.util.ArrayList;
import java.util.List;


public class ScanResponse {
    private List<ScanInfo> scans = new ArrayList();

    public ScanResponse(ApiResponseList responseList) {
        for (ApiResponse rawResponse : responseList.getItems()) {
            scans.add(new ScanInfo((ApiResponseSet)rawResponse));
        }
    }

    public List<ScanInfo> getScans() {
        return scans;
    }

    public ScanInfo getScanById(int scanId) {
        for (ScanInfo scan : scans) {
            if (scan.getId() == scanId) return scan;
        }
        return null;
    }

    public ScanInfo getLastScan() {
        return scans.get(scans.size()-1);
    }
}

package com.bsu.famcs.wscan.zap;

import com.bsu.famcs.wscan.zap.model.ScanInfo;
import com.bsu.famcs.wscan.zap.model.ScanResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.zaproxy.clientapi.core.*;

import java.util.List;

@Component
public class Scanner {
    private final static Logger logger = Logger.getLogger(Scanner.class);

    @Autowired
    private ClientApi clientApi;

    @Autowired
    private @Value("${API_KEY}") String apiKey;


    public ApiResponse runSpider(String url){
        final String MAX_CHILDREN_SCAN = "5";
        final String IS_RECURSIVE = "true";
        try {
            return clientApi.spider.scan(apiKey,url,MAX_CHILDREN_SCAN, IS_RECURSIVE, null);
        } catch (ClientApiException e) {
            logger.error("API Spider scan failed",e);
            throw new ZapException(e);
        }
    }

    public ScanResponse getSpiderAllScansProgress(){
        try {
            ApiResponseList response = (ApiResponseList)clientApi.spider.scans();
            return new ScanResponse(response);
        } catch (ClientApiException e) {
            logger.error("API Spider failed", e);
            throw new ZapException(e);
        }
    }

    public void runAnalyzer(String url){
        try {
            clientApi.ascan.scan(apiKey,url, "true", "false",null,null,null);
        } catch (ClientApiException e) {
            logger.error("API scanner failed", e);
            throw new ZapException(e);
        }
    }
    public ScanResponse getAllAnalyzersProgress(){
        try {
            ApiResponseList response = (ApiResponseList)clientApi.ascan.scans();
            return new ScanResponse(response);
        } catch (ClientApiException e) {
            logger.error("API scanner failed", e);
            throw new ZapException(e);
        }
    }

    public boolean isSpiderAllScansDone(){
        ScanResponse currentScannersResp = getSpiderAllScansProgress();
        return isScannerDone(currentScannersResp);
    }
    public boolean isAllAnalyzersDone(){
        ScanResponse currentScannersResp = getAllAnalyzersProgress();
        return isScannerDone(currentScannersResp);
    }

    public List<Alert> getAllAlerts(String url){
        try {
            return clientApi.getAlerts(url,-1,-1);
        } catch (ClientApiException e) {
            logger.error("API failed", e);
            throw new ZapException(e);
        }
    }
    public byte[] getXmlReport() {
        try {
            return clientApi.core.xmlreport(apiKey);
        } catch (ClientApiException e) {
            e.printStackTrace();
            throw new ZapException(e);
        }
    }
    private boolean isScannerDone(ScanResponse response){
        int i = 0;
        for(ScanInfo scanInfo: response.getScans()){
            i++;
            if(scanInfo.getProgress() != 100){
                System.out.println(i+"/"+response.getScans().size()+" "+scanInfo.getProgress());
                return false;
            }
        }
        return true;
    }
}

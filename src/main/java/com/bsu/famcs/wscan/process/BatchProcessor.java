package com.bsu.famcs.wscan.process;

import com.bsu.famcs.wscan.config.ApplConfiguration;
import com.bsu.famcs.wscan.model.SiteDataTreatment;
import com.bsu.famcs.wscan.model.SiteData;
import com.bsu.famcs.wscan.reporting.XMLReporting;
import com.bsu.famcs.wscan.service.IBatchService;
import com.bsu.famcs.wscan.zap.Scanner;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import org.zaproxy.clientapi.core.Alert;

import java.util.Collection;
import java.util.List;
import java.util.Map;

//gaidukan@bsu.by
@Component
public class BatchProcessor {
    private static final Logger logger = Logger.getLogger(BatchProcessor.class);

    @Autowired
    private IBatchService batchService;
    @Autowired
    private Scanner scanner;
    @Autowired
    private XMLReporting reporting;

    private SiteDataTreatment siteDataTreatment;


    public void doWork(){
        siteDataTreatment = new SiteDataTreatment();
        batchService.importBatch(siteDataTreatment);

        Map<String,SiteData> mapSiteDataStorage = siteDataTreatment.getMapSiteData();


   //     runSpider(mapSiteDataStorage.values());
     //   runSecurityScanner(mapSiteDataStorage.values());

        parseBatchScanResult(mapSiteDataStorage);

        byte[] xmlReport = scanner.getXmlReport();

        reporting.exportFullReport(xmlReport, siteDataTreatment.getReportingOutputGenName());
        reporting.exportMainStatistic(siteDataTreatment);

    }

    private void parseBatchScanResult(Map<String,SiteData> mapSiteDataStorage){
        mapSiteDataStorage.values().forEach(siteData -> {

            System.out.println(siteData.getSiteLink());
            List<Alert> listSiteScanResult = scanner.getAllAlerts(siteData.getSiteLink());
            parseSiteScanResult(listSiteScanResult,siteData);
        });

    }
    //TODO really parse data and remove addiction from lib classes
    private void parseSiteScanResult(List<Alert> listScanResult, SiteData siteData){
        siteData.setScanResults(listScanResult);
        int lowRiskCount = 0;
        int mediumRiskCount = 0;
        int highRiskCount = 0;

        for(Alert alert :listScanResult ){
            if(alert.getRisk() == Alert.Risk.Low){
                lowRiskCount++;
            }
            if(alert.getRisk() == Alert.Risk.Medium){
                mediumRiskCount++;
            }
            if(alert.getRisk() == Alert.Risk.High){
                highRiskCount++;
            }
        }
        siteData.setLowRiskCount(lowRiskCount);
        siteData.setMediumRiskCount(mediumRiskCount);
        siteData.setHighRiskCount(highRiskCount);
    }
    //TODO modify to split processing sites
    private void runSpider(Collection<SiteData> listSiteData){
        listSiteData.forEach(siteData ->{
            scanner.runSpider(siteData.getSiteLink());
        });

        while (!scanner.isSpiderAllScansDone()){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.warn("Thread sleep error", e);
            }
        }
    }

    private void runSecurityScanner(Collection<SiteData> listSiteData){
        listSiteData.forEach(siteData ->{
            scanner.runAnalyzer(siteData.getSiteLink());
        });

        while (!scanner.isAllAnalyzersDone()){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.warn("Thread sleep error", e);
            }
        }
    }

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplConfiguration.class);

        BatchProcessor bp = (BatchProcessor)context.getBean(BatchProcessor.class);

        bp.doWork();

    }
}

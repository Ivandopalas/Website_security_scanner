package com.bsu.famcs.wscan.service.impl;

import com.bsu.famcs.wscan.model.CSVBatch;
import com.bsu.famcs.wscan.model.SiteDataTreatment;
import com.bsu.famcs.wscan.model.SiteData;
import com.bsu.famcs.wscan.repository.IBatchLoad;
import com.bsu.famcs.wscan.repository.impl.CSVHeaderHelper;
import com.bsu.famcs.wscan.service.IBatchService;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CSVBatchService implements IBatchService{
    @Autowired
    private IBatchLoad batchLoad;

    public boolean importBatch(SiteDataTreatment siteDataTreatment){
        final CSVBatch batch = (CSVBatch)batchLoad.retrieveUnprocessedBatch();

        if(batch.isEmpty()){
            return false;
        }
        if(siteDataTreatment == null){
            throw new IllegalArgumentException();
        }

        List<CSVRecord> listBatchData = batch.getBatchData();

        siteDataTreatment.init();

        Map<String,SiteData> mapSiteData = siteDataTreatment.getMapSiteData();

        String reportingGenName = batch.getBatchName();
        reportingGenName.replace(".csv","_report.xml");

        siteDataTreatment.setReportingOutputGenName(reportingGenName);
        fillSiteData(listBatchData, mapSiteData);

        return true;
    }

    private void fillSiteData(List<CSVRecord> listBatchData,Map<String,SiteData> mapSiteData){
        listBatchData.forEach(CSVRecord ->{
            String siteLink = CSVRecord.get(CSVHeaderHelper.SITE_LINK_HEADER);
            String siteCategory = CSVRecord.get(CSVHeaderHelper.SITE_CATEGORY_HEADER);

            SiteData siteDataRecord = new SiteData();
            siteDataRecord.setSiteLink(siteLink);
            siteDataRecord.setSiteCategory(siteCategory);

            mapSiteData.put(siteLink, siteDataRecord);
        });
    }

}

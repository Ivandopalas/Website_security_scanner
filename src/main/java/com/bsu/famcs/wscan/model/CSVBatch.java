package com.bsu.famcs.wscan.model;

import org.apache.commons.csv.CSVRecord;

import java.util.ArrayList;
import java.util.List;

public class CSVBatch extends Batch<CSVRecord> {

    public void pushRecord(CSVRecord record){
        if(this.batchData == null){
            this.batchData = new ArrayList<>();
        }
        this.batchData.add(record);
    }

    @Override
    public List<CSVRecord> getBatchData() {
        return this.batchData;
    }

    @Override
    public void setBatchData(List<CSVRecord> batchData) {
        this.batchData = batchData;
    }
}

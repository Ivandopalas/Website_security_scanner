package com.bsu.famcs.wscan.model;

import java.util.List;

public abstract class Batch <DataType>{
    private String batchName;
    protected List<DataType> batchData;

    public abstract List<DataType> getBatchData();
    public abstract void setBatchData(List<DataType> batchData);

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public boolean isEmpty(){
        return batchData.isEmpty();
    }

}

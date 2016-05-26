package com.bsu.famcs.wscan.repository;

import com.bsu.famcs.wscan.model.Batch;

public interface IBatchLoad {
    public final static String UNPROCESSED_STATUS = "unprocessed";
    public final static String PROCESSING_STATUS = "processing";
    public final static String PROCESSED_STATUS = "processed";

    Batch retrieveUnprocessedBatch();

}

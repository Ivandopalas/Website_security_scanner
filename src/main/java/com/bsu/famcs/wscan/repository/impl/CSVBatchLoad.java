package com.bsu.famcs.wscan.repository.impl;

import com.bsu.famcs.wscan.model.CSVBatch;
import com.bsu.famcs.wscan.repository.IBatchLoad;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Repository
public class CSVBatchLoad implements IBatchLoad {
    private final static Logger logger = Logger.getLogger(CSVBatchLoad.class);

    @Value("${BATCH_PATH}")
    private String batchPath ;

    public CSVBatch retrieveUnprocessedBatch() {
        CSVBatch batch = new CSVBatch();
        final File folderBatch = new File(batchPath);

        if (folderBatch != null) {

            for (File fileBatch : folderBatch.listFiles()) {

                if (fileBatch != null &&
                        fileBatch.isFile() &&
                        fileBatch.getName().endsWith("unprocessed")) {

                    if(loadBatch(batch, fileBatch)){
                        setProcessingStatus(fileBatch);
                    }
                }
            }
        }
        return batch;
    }

    private void setProcessingStatus(File fileBatch){
        String updatedBatchName = fileBatch.getName().replace(IBatchLoad.UNPROCESSED_STATUS, IBatchLoad.PROCESSING_STATUS);
        File updatedStatusFileBatch = new File(batchPath+updatedBatchName);
        if(!fileBatch.renameTo(updatedStatusFileBatch)){
            logger.warn("Can't set batch file status to " + IBatchLoad.PROCESSING_STATUS);
        }
    }
    private boolean loadBatch(CSVBatch batch, final File fileBatch){
        try {
            CSVFormat fileBatchFormat = CSVFormat.DEFAULT
                    .withHeader(CSVHeaderHelper.FILE_BATCH_HEADER_MAPPING)
                    .withSkipHeaderRecord(true);
            CSVParser batchParser = CSVParser.parse(fileBatch, StandardCharsets.UTF_8, fileBatchFormat);
            if(batch != null) {
                batch.setBatchName(fileBatch.getName().replace(IBatchLoad.PROCESSED_STATUS,""));
                batch.setBatchData(batchParser.getRecords());
            }
        } catch (IOException ex) {
            logger.error("Can't find batch file " + fileBatch.getName(), ex);
            return false;
        }
        return true;
    }
}

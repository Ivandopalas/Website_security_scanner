package com.bsu.famcs.wscan.service;

import com.bsu.famcs.wscan.model.SiteDataTreatment;

public interface IBatchService {

    public boolean importBatch(SiteDataTreatment siteDataTreatment);
}

package com.ddout.fb.service.updata.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.ddout.fb.service.updata.IUpDataService;

public class FlushTaskJob {

    public static Logger log = Logger.getLogger(FlushTaskJob.class);

    @Autowired
    private IUpDataService service;

    public void exec() {
	log.debug("BEGIN------------- FlushTaskJob");
	try {
	    service.updateNewMatch();
	} catch (Exception e) {
	    log.error(e);
	}
	try {
	    service.updateNewSeason();
	} catch (Exception e) {
	    log.error(e);
	}
	try {
	    service.updateOldMatch();
	} catch (Exception e) {
	    log.error(e);
	}
	log.debug("END---------------- FlushTaskJob");
    }

}
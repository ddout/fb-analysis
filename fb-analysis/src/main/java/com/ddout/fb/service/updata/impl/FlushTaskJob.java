package com.ddout.fb.service.updata.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.cdhy.commons.utils.SysConfigUtil;
import com.ddout.fb.service.updata.IUpDataService;

public class FlushTaskJob {

    public static Logger log = Logger.getLogger(FlushTaskJob.class);
    private static final String IS_LOAD_FLUSH = SysConfigUtil.getInstance().getProperites("is_load_flush");
    @Autowired
    private IUpDataService service;

    public void exec() {
	log.debug("BEGIN------------- FlushTaskJob");
	if ("false".equals(IS_LOAD_FLUSH)) {
	    return;
	}
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
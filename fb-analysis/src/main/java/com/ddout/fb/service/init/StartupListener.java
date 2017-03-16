package com.ddout.fb.service.init;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.ddout.fb.service.mongodb.IMongoDBService;
import com.ddout.fb.service.parse.ICrawlerService;

import net.sf.json.JSONObject;

@Component
public class StartupListener implements ApplicationContextAware {
    private static final Logger log = Logger.getLogger(StartupListener.class);
    @Autowired
    private ICrawlerService service;
    @Autowired
    private IMongoDBService dbService;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
	new Thread(new Runnable() {
	    @Override
	    public void run() {
		JSONObject systemInfo = dbService.getSystemInfo();
		if (null != systemInfo) {
		    log.info("system is inited!");
		    return;
		}
		log.info("StartupListener   init begin ~~~~~~~~~~~~~~~~~~~~~~");
		service.parseCountry();
		log.info("parseCountry   parse end!");
		//
		log.info("parseSeasonAndTeam   init begin ~~~~~~~~~~~~~~~~~~~~~~");
		List<JSONObject> countrys = dbService.queryAllObject(IMongoDBService.COLNAME_COUNTRY);
		service.parseSeasonAndTeam(countrys);
		log.info("parseSeasonAndTeam   parse end!");
		//
		log.info("parseGames   init begin ~~~~~~~~~~~~~~~~~~~~~~");
		List<JSONObject> seasons = dbService.queryAllObject(IMongoDBService.COLNAME_SEASON);
		service.parseGames(seasons);
		log.info("parseGames  parse end!");
		///
		///
		JSONObject json = new JSONObject();
		Date initTime = new Date();
		json.put("system_id", "fb-analysis");
		json.put("initTime", initTime.getTime());
		json.put("initTimeStr", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(initTime));
		dbService.saveSystemInfo(json);
		log.info("system is inited-----!" + initTime);
	    }
	}).start();

    }

}

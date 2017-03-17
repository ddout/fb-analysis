package com.ddout.fb.service.init;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.ddout.fb.service.ICust;
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
		if (null == systemInfo) {
		    JSONObject json = new JSONObject();
		    Date initTime = new Date();
		    json.put(ICust.SYSTEM_CONFIG_ID, ICust.SYSTEM_CONFIG_NAME);
		    json.put("initTime", initTime.getTime());
		    json.put("initTimeStr", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(initTime));
		    dbService.saveSystemInfo(json);
		    systemInfo = dbService.getSystemInfo();
		} 
		//
		//
		if (!systemInfo.containsKey("init_country")
			|| !"finally".equals(systemInfo.getString("init_country"))) {
		    log.info("StartupListener   init begin ~~~~~~~~~~~~~~~~~~~~~~");
		    service.parseCountry();
		    //
		    Criteria criatiraTeam = new Criteria();
		    criatiraTeam.andOperator(Criteria.where(ICust.SYSTEM_CONFIG_ID).is(ICust.SYSTEM_CONFIG_NAME));
		    Update update = new Update();
		    update.set("init_country", "finally");
		    dbService.updateObj(criatiraTeam, update, ICust.SYSTEM_INFO);
		    log.info("parseCountry   parse end!");
		} else {
		    log.info("parseCountry is parse finally  !");
		}
		//
		//
		if (!systemInfo.containsKey("init_SeasonAndTeam")
			|| !"finally".equals(systemInfo.getString("init_SeasonAndTeam"))) {
		    log.info("parseSeasonAndTeam   init begin ~~~~~~~~~~~~~~~~~~~~~~");
		    List<JSONObject> countrys = dbService.queryAllObject(ICust.COLNAME_COUNTRY);
		    service.parseSeasonAndTeam(countrys);
		    //
		    Criteria criatiraTeam = new Criteria();
		    criatiraTeam.andOperator(Criteria.where(ICust.SYSTEM_CONFIG_ID).is(ICust.SYSTEM_CONFIG_NAME));
		    Update update = new Update();
		    update.set("init_SeasonAndTeam", "finally");
		    dbService.updateObj(criatiraTeam, update, ICust.SYSTEM_INFO);
		    log.info("parseSeasonAndTeam   parse end!");
		} else {
		    log.info("SeasonAndTeam is parse finally  !");
		}
		//
		if (!systemInfo.containsKey("init_Games") || !"finally".equals(systemInfo.getString("init_Games"))) {
		    log.info("parseGames   init begin ~~~~~~~~~~~~~~~~~~~~~~");
		    final List<JSONObject> seasons = dbService.queryAllObject(ICust.COLNAME_SEASON);
		    // 开线程
		    final int threadMax = 99;
		    final int threadCount = Math.round(seasons.size() / threadMax);
		    final int threadLoad = seasons.size() % threadMax;
		    for (int i = 0; i < threadMax; i++) {
			final int x = i;
			new Thread(new Runnable() {
			    @Override
			    public void run() {
				List<JSONObject> subList = seasons.subList(threadCount * x, threadCount * (x + 1));
				service.parseGames(subList);
				//
				JSONObject threadGroupObj = new JSONObject();
				threadGroupObj.put("group_id", x);
				threadGroupObj.put("group_name", ICust.SYSTEM_INIT_GAMES_THREAD_GROUP);
				dbService.saveObject(threadGroupObj, ICust.SYSTEM_INIT_GAMES_THREAD_GROUP);
			    }
			}).start();
		    }
		    if (threadLoad > 0) {
			new Thread(new Runnable() {
			    @Override
			    public void run() {
				List<JSONObject> subList = seasons.subList(threadCount * threadMax, seasons.size());
				service.parseGames(subList);
				//
				JSONObject threadGroupObj = new JSONObject();
				threadGroupObj.put("group_id", threadMax + 1);
				threadGroupObj.put("group_name", ICust.SYSTEM_INIT_GAMES_THREAD_GROUP);
				dbService.saveObject(threadGroupObj, ICust.SYSTEM_INIT_GAMES_THREAD_GROUP);
			    }
			}).start();
		    }
		    //
		    Criteria criatira = new Criteria();
		    criatira.andOperator(Criteria.where("group_name").is(ICust.SYSTEM_INIT_GAMES_THREAD_GROUP));
		    long groupCount = 0;
		    do {
			groupCount = dbService.getCount(criatira, ICust.SYSTEM_INIT_GAMES_THREAD_GROUP);
			try {
			    Thread.sleep(1000 * 10);
			} catch (InterruptedException e) {
			    e.printStackTrace();
			}
		    } while (groupCount < (threadMax + 1));

		    Criteria criatiraTeam = new Criteria();
		    criatiraTeam.andOperator(Criteria.where(ICust.SYSTEM_CONFIG_ID).is(ICust.SYSTEM_CONFIG_NAME));
		    Update update = new Update();
		    update.set("init_Games", "finally");
		    dbService.updateObj(criatiraTeam, update, ICust.SYSTEM_INFO);
		    log.info("parseGames  parse end!");
		} else {
		    log.info("Games is parse finally  !");
		}
		///
		log.info("system is inited-----!");
	    }
	}).start();

    }
}

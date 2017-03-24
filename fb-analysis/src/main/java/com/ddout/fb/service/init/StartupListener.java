package com.ddout.fb.service.init;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.cdhy.commons.utils.ParamsUtil;
import com.cdhy.commons.utils.SysConfigUtil;
import com.ddout.fb.dao.fb.ISystemInfoMapper;
import com.ddout.fb.service.ICust;
import com.ddout.fb.service.parse.ICrawlerService;

@SuppressWarnings("serial")
@Component
public class StartupListener implements ApplicationContextAware {
    private static final Logger log = Logger.getLogger(StartupListener.class);
    private static final String IS_LOAD_INIT = SysConfigUtil.getInstance().getProperites("is_load_init");
    @Autowired
    private ICrawlerService service;
    @Autowired
    private ISystemInfoMapper systemInfoMapper;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
	if ("false".equals(IS_LOAD_INIT)) {
	    return;
	}
	Map<String, Object> systemInfoObj = systemInfoMapper.getSystemInfo();
	if (null == systemInfoObj) {
	    Map<String, Object> json = new HashMap<String, Object>();
	    json.put(ICust.SYSTEM_CONFIG_ID, ICust.SYSTEM_CONFIG_NAME);
	    json.put("initTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	    systemInfoMapper.saveSystemInfo(json);
	    systemInfoObj = systemInfoMapper.getSystemInfo();
	}
	final Map<String, Object> systemInfo = systemInfoObj;
	new Thread(new Runnable() {
	    @Override
	    public void run() {
		//
		//
		if (!systemInfo.containsKey("init_country")
			|| !"finally".equals(ParamsUtil.getString4Map(systemInfo, "init_country"))) {
		    log.info("StartupListener   init begin ~~~~~~~~~~~~~~~~~~~~~~");
		    service.parseCountry();
		    //
		    systemInfoMapper.updateInitCountry();
		    log.info("parseCountry   parse end!");
		} else {
		    log.info("parseCountry is parse finally  !");
		}
		//
		//
		if (!systemInfo.containsKey("init_SeasonAndTeam")
			|| !"finally".equals(ParamsUtil.getString4Map(systemInfo, "init_SeasonAndTeam"))) {
		    log.info("parseSeasonAndTeam   init begin ~~~~~~~~~~~~~~~~~~~~~~");
		    final List<Map<String, Object>> countrys = systemInfoMapper.queryAllCountry();
		    // 开线程
		    final int threadMax = 20;
		    final int threadCount = Math.round(countrys.size() / threadMax);
		    final int threadLoad = countrys.size() % threadMax;
		    for (int i = 0; i < threadMax; i++) {
			final int x = i;
			new Thread(new Runnable() {
			    @Override
			    public void run() {
				List<Map<String, Object>> subList = countrys.subList(threadCount * x,
					threadCount * (x + 1));
				service.parseSeasonAndTeam(subList);
				//
				systemInfoMapper.saveThreadGroup4SeasonAndTeam(new HashMap<String, Object>() {
				    {
					put("group_id", x);
				    }
				});
			    }
			}).start();
		    }
		    if (threadLoad > 0) {
			new Thread(new Runnable() {
			    @Override
			    public void run() {
				List<Map<String, Object>> subList = countrys.subList(threadCount * threadMax,
					countrys.size());
				service.parseSeasonAndTeam(subList);
				//
				systemInfoMapper.saveThreadGroup4SeasonAndTeam(new HashMap<String, Object>() {
				    {
					put("group_id", threadMax + 1);
				    }
				});
			    }
			}).start();
		    }
		    //
		    int groupCount = 0;
		    do {
			log.info("tttt=[" + (groupCount < threadMax) + "]=[" + groupCount + "][" + threadMax + "]");
			groupCount = systemInfoMapper.queryThreadGroup4SeasonAndTeamCount();
			try {
			    Thread.sleep(1000 * 20);
			} catch (InterruptedException e) {
			    e.printStackTrace();
			}
		    } while (groupCount < threadMax);
		    //
		    systemInfoMapper.updateInitSeasonAndTeam();
		    log.info("parseSeasonAndTeam   parse end!");
		} else {
		    log.info("SeasonAndTeam is parse finally  !");
		}
		//
		if (!systemInfo.containsKey("init_Games")
			|| !"finally".equals(ParamsUtil.getString4Map(systemInfo, "init_Games"))) {
		    log.info("parseGames   init begin ~~~~~~~~~~~~~~~~~~~~~~");
		    final List<Map<String, Object>> seasons = systemInfoMapper.queryAllSeason();
		    // 开线程
		    final int threadMax = 39;
		    final int threadCount = Math.round(seasons.size() / threadMax);
		    final int threadLoad = seasons.size() % threadMax;
		    for (int i = 0; i < threadMax; i++) {
			final int x = i;
			new Thread(new Runnable() {
			    @Override
			    public void run() {
				List<Map<String, Object>> subList = seasons.subList(threadCount * x,
					threadCount * (x + 1));
				service.parseGames(subList);
				//
				systemInfoMapper.saveThreadGroup4Games(new HashMap<String, Object>() {
				    {
					put("group_id", x);
				    }
				});
			    }
			}).start();
		    }
		    if (threadLoad > 0) {
			new Thread(new Runnable() {
			    @Override
			    public void run() {
				List<Map<String, Object>> subList = seasons.subList(threadCount * threadMax,
					seasons.size());
				service.parseGames(subList);
				//
				systemInfoMapper.saveThreadGroup4Games(new HashMap<String, Object>() {
				    {
					put("group_id", threadMax + 1);
				    }
				});
			    }
			}).start();
		    }
		    //
		    int groupCount = 0;
		    do {
			log.info("tttt=[" + (groupCount < threadMax) + "]=[" + groupCount + "][" + threadMax + "]");
			groupCount = systemInfoMapper.queryThreadGroup4GamesCount();
			try {
			    Thread.sleep(1000 * 20);
			} catch (InterruptedException e) {
			    e.printStackTrace();
			}
		    } while (groupCount < threadMax);

		    systemInfoMapper.updateInitGames();
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

package com.ddout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.cdhy.commons.utils.ParamsUtil;
import com.ddout.fb.dao.fb.ISystemInfoMapper;
import com.ddout.fb.service.ICust;
import com.ddout.fb.service.mysql.ISaveDataService;
import com.ddout.fb.service.parse.ICrawlerService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({ "classpath*:conf/spring*.xml" })
public class TestCrawler {
    @Autowired
    private ICrawlerService service;
    @Autowired
    private ISaveDataService dbService;
    @Autowired
    private ISystemInfoMapper systemInfoMapper;

    @Test
    public void testDB() {
	JSONObject json = new JSONObject();
	json.put("matchName", "测试国家");// 国家名称
	json.put("matchInfoURI", "kjl/sdfsdf/sdfsdf");
	json.put("region", "欧洲");
	json.put("league", new JSONArray());
	try {
	    dbService.saveCountry(json);
	} catch (Exception e) {
	    e.printStackTrace();
	    throw e;
	}
    }

    @Test
    public void testParseCountry() {
	try {
	    service.parseCountry();
	} catch (Exception e) {
	    e.printStackTrace();
	    throw e;
	}
    }

    @Test
    public void testParseSeasonAndTeam() {
	try {
	    // List<Map<String, Object>> leagues = new ArrayList<Map<String,
	    // Object>>();
	    // Map<String, Object> league = new HashMap<String, Object>();
	    // league.put("region", "欧洲");
	    // league.put("matchName", "英格兰");
	    // league.put("leagueName", "英超");
	    // league.put("leagueURI", "/soccer/league/17/");
	    // leagues.add(league);
	    List<Map<String, Object>> countrys = systemInfoMapper.queryAllCountry();
	    List<Map<String, Object>> subList = countrys.subList(0, 2);
	    service.parseSeasonAndTeam(subList);
	} catch (Exception e) {
	    e.printStackTrace();
	    throw e;
	}
    }

    @Test
    public void testParseMatch() {
	try {
	    List<Map<String, Object>> seasons = new ArrayList<Map<String, Object>>();
	    Map<String, Object> season = new HashMap<String, Object>();
	    season.put("region", "欧洲");
	    season.put("matchName", "英格兰");
	    season.put("leagueName", "英超");
	    season.put("seasonName", "英超 16/17 赛季");
	    season.put("seasonURI", "/soccer/league/17/schedule/12651/");
	    seasons.add(season);
	    service.parseGames(seasons);
	} catch (Exception e) {
	    e.printStackTrace();
	    throw e;
	}
    }

}

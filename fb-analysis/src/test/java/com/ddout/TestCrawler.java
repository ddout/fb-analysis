package com.ddout;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.ddout.fb.service.mongodb.IMongoDBService;
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
    private IMongoDBService dbService;

    @Test
    public void testDB() {
	Object o = dbService.queryTest();
	System.out.println(o);
    }

    /**
     * 解析国家-联赛
     */
    @Test
    public void testParseCountry() {
	JSONArray array = service.parseCountry();
	System.out.println(array);
    }

    /**
     * 解析赛季-球队
     */
    @Test
    public void testParseSeason() {
	List<JSONObject> countrys = dbService.queryAllObject(IMongoDBService.COLNAME_COUNTRY);
	JSONArray array = service.parseSeasonAndTeam(countrys);
	System.out.println(array);
    }

    /**
     * 解析比赛
     */
    @Test
    public void testParseGames() {
	List<JSONObject> countrys = dbService.queryAllObject(IMongoDBService.COLNAME_SEASON);
	List<JSONObject> cc = countrys.subList(0, 1);
	service.parseGames(cc);
    }
}

package com.ddout;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.ddout.fb.service.ICust;
import com.ddout.fb.service.mongodb.IMongoDBService;
import com.ddout.fb.service.parse.ICrawlerService;
import com.ddout.fb.service.updata.IUpDataService;

import net.sf.json.JSONObject;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({ "classpath*:conf/spring*.xml" })
public class TestCrawler {
    @Autowired
    private ICrawlerService service;
    @Autowired
    private IMongoDBService dbService;

    @Autowired
    private IUpDataService upDataService;

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
	service.parseCountry();
    }

    /**
     * 解析赛季-球队
     */
    @Test
    public void testParseSeason() {
	List<JSONObject> countrys = dbService.queryAllObject(ICust.COLNAME_COUNTRY);
	service.parseSeasonAndTeam(countrys);
    }

    /**
     * 解析比赛
     */
    @Test
    public void testParseGames() {
	List<JSONObject> countrys = dbService.queryAllObject(ICust.COLNAME_SEASON);
	List<JSONObject> cc = countrys.subList(0, 1);
	service.parseGames(cc);
    }

    /**
     * 更新比赛
     */
    @Test
    public void testUpDataService() {
	upDataService.updateOldMatch();
    }
}

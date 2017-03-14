package com.ddout;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.ddout.fb.service.analysis.ICrawlerService;
import com.ddout.fb.service.mongodb.IMongoDBService;

import net.sf.json.JSONArray;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({ "classpath*:conf/spring*.xml" })
public class TestCrawler {
    @Autowired
    private ICrawlerService service;
    @Autowired
    private IMongoDBService mongoDBService;

    @Test
    public void testParseCountry() throws Exception {
	JSONArray array = service.parseCountry();
	System.out.println(array);
    }

}

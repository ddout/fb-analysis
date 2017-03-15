package com.ddout;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.ddout.fb.service.parse.ISerializeForDB;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({ "classpath*:conf/spring*.xml" })
public class TestSerialize {
    @Autowired
    private ISerializeForDB service;

    /**
     * 解析国家-联赛
     */
    @Test
    public void testParseCountry() {
	service.saveCountry();
    }

    /**
     * 解析赛季
     */
    @Test
    public void testParseSeason() {

	// JSONArray array = service.parseSeason(league);
	// System.out.println(array);
    }
}

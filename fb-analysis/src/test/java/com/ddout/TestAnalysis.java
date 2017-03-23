package com.ddout;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.ddout.fb.service.view.IAnalysisService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({ "classpath*:conf/spring*.xml" })
public class TestAnalysis {
    @Autowired
    private IAnalysisService service;

    public static String matchId = "123123";

    @Test
    public void testAnalysis() {
	service.execAnalysis(matchId);
    }

    @Test
    public void testHisBattle() {
	service.execHisBattle(matchId);
    }

    @Test
    public void testHomeState() {
	service.execHomeState(matchId);
    }

    @Test
    public void testAwayState() {
	service.execAwayState(matchId);
    }

    @Test
    public void testIdxAnalysis() {
	service.execIdxAnalysis(matchId);
    }

}

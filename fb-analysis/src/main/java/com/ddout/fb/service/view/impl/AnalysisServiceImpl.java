package com.ddout.fb.service.view.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ddout.fb.dao.fb.IAnalysisMapper;
import com.ddout.fb.service.view.IAnalysisService;

@Service
public class AnalysisServiceImpl implements IAnalysisService {
    @Autowired
    private IAnalysisMapper mapper;

    @Override
    public Map<String, Object> execAnalysis(String matchId) {
	// TODO 这里应该是把其他的分析结果综合起来，按照一定的权重，综合得出分析结果(可以按照百分比呈现)
	// 目前想到6种
	Map<String, Object> result = new HashMap<String, Object>();
	// result.put("analysis", execAnalysis(matchId));// 最终分析结果--包含实际结果的对比
	// result.put("his_battle", execHisBattle(matchId));// 历史交锋分析结果
	// result.put("home_state", execHomeState(matchId));// 最近比赛状态分析(主)
	// result.put("away_state", execAwayState(matchId));// 最近比赛状态分析(客)
	// result.put("idx_analysis", execIdxAnalysis(matchId));// 指数分析结果
	return result;
    }

    @Override
    public Object execHisBattle(String matchId) {
	// TODO 历史交锋分析结果
	//1.分主客场???
	return null;
    }

    @Override
    public Object execHomeState(String matchId) {
	// TODO 最近比赛状态分析(主)
	return null;
    }

    @Override
    public Object execAwayState(String matchId) {
	// TODO 最近比赛状态分析(客)
	return null;
    }

    @Override
    public Object execIdxAnalysis(String matchId) {
	// TODO 指数分析结果
	return null;
    }

    @Override
    public Object execSameLeagueHome(String matchId) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Object execSameLeagueAway(String matchId) {
	// TODO Auto-generated method stub
	return null;
    }

}

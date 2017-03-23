package com.ddout.fb.service.view;

import java.util.Map;

public interface IAnalysisService {
    /**
     * 最终分析结果--包含实际结果的对比
     * 
     * @param matchId
     * @return
     */
    Map<String, Object> execAnalysis(String matchId);

    /**
     * 历史交锋分析结果
     * 
     * @param matchId
     * @return
     */
    Object execHisBattle(String matchId);

    /**
     * 最近比赛状态分析(主)
     * 
     * @param matchId
     * @return
     */
    Object execHomeState(String matchId);

    /**
     * 最近比赛状态分析(客)
     * 
     * @param matchId
     * @return
     */
    Object execAwayState(String matchId);

    /**
     * 指数分析结果
     * 
     * @param matchId
     * @return
     */
    Object execIdxAnalysis(String matchId);
    /**
     * 同类型赛事情况(主队)
     * 
     * @param matchId
     * @return
     */
    Object execSameLeagueHome(String matchId);
    /**
     * 同类型赛事情况(客队)
     * 
     * @param matchId
     * @return
     */
    Object execSameLeagueAway(String matchId);
}

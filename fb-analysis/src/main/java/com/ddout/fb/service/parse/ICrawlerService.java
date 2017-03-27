package com.ddout.fb.service.parse;

import java.util.List;
import java.util.Map;

public interface ICrawlerService {
    /**
     * 抓取国家/联赛信息
     * 
     * @return
     */
    public void parseCountry();

    /**
     * 解析赛季-球队信息
     * 
     * @param leagues
     * @return
     */
    public void parseSeasonAndTeam(List<Map<String, Object>> leagues);

    /**
     * 解析比赛
     * 
     * @param seasons
     *            赛季
     * @return
     */
    public void parseGames(List<Map<String, Object>> seasons);

    /**
     * 解析最新的一个zucai14
     * 
     * @return
     */
    public void parseZucai14();

    public void parseZucai14Old(String no);
}

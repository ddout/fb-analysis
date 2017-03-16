package com.ddout.fb.service.parse;

import java.util.List;

import net.sf.json.JSONObject;

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
     * @param countrys
     * @return
     */
    public void parseSeasonAndTeam(List<JSONObject> countrys);

    /**
     * 解析比赛
     * 
     * @param seasons
     *            赛季
     * @return
     */
    public void parseGames(List<JSONObject> seasons);
}
